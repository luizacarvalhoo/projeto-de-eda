#!/usr/bin/python3

import PyQt5
import PyQt5.QtWidgets

from PyQt5.QtCore import QDateTime, Qt, QTimer
from PyQt5.QtWidgets import (QApplication, QCheckBox, QComboBox, QDateTimeEdit,
        QDial, QDialog, QGridLayout, QGroupBox, QHBoxLayout, QLabel, QLineEdit,
        QProgressBar, QPushButton, QRadioButton, QScrollBar, QSizePolicy,
        QSlider, QSpinBox, QStyleFactory, QTableWidget, QTabWidget, QTextEdit,
                             QVBoxLayout, QWidget, QTableWidgetItem)
from PyQt5.QtCore import QObject, QThread, pyqtSignal
from popularity_traffic_mixer import *
from popularity_trace_generator import *
from arg_util import *


## A worker class
class Worker(QObject):
    finished = pyqtSignal()
    progress = pyqtSignal(int)

    def setargs(self, args):
        self.args = args

    def setPrintBox(self, printBox):
        self.printBox = printBox
        
    def run(self):
        self.trafficMixer    = TrafficMixer(self.args, self.printBox)
        self.traceGenerator  = TraceGenerator(self.trafficMixer, self.args, self.printBox)
        self.traceGenerator.generate()                    
        self.finished.emit()



class WidgetGallery(QDialog):
    def __init__(self, parent=None):

        super(WidgetGallery, self).__init__(parent)

        ## Read the available traffic classes from the file in FOOTPRINT_DESCRIPTORS/available_tcs.txt
        self.readAvailableTrafficClasses()

        ## Setups format and styling for the GUI
        self.setGeometry(300, 150, 350, 500)
        self.originalPalette = QApplication.palette()
        styleComboBox = QComboBox()
        styleComboBox.addItems(QStyleFactory.keys())
        styleLabel = QLabel("&Style:")
        styleLabel.setBuddy(styleComboBox)
        self.useStylePaletteCheckBox = QCheckBox("&Use style's standard palette")
        self.useStylePaletteCheckBox.setChecked(True)
        disableWidgetsCheckBox = QCheckBox("&Disable widgets")
        styleComboBox.activated[str].connect(self.changeStyle)
        self.useStylePaletteCheckBox.toggled.connect(self.changePalette)

        ## Create the various widgets and add populate the table
        self.createTraceLengthTextField()
        self.createTrafficVolumeBox()
        self.createTrafficClassList()
        self.createProgressBar()

        disableWidgetsCheckBox.toggled.connect(self.topRightGroupBox.setDisabled)        

        self.submitButton = QPushButton("4. Generate")
        self.submitButton.clicked.connect(self.go)
        
        self.printStatus = QLineEdit(self)
        self.printStatus.setText("Ready ...")
        self.printStatus.setEnabled(False)
        
        mainLayout = QGridLayout()
        mainLayout.addWidget(self.topRightGroupBox, 1, 0, 1, 2)
        mainLayout.addWidget(self.trafficVolumeBox, 2, 0, 1, 2)
        mainLayout.addWidget(self.bottomLeftTabWidget, 3, 0, 1, 3)
        mainLayout.addWidget(self.submitButton, 4, 0)
        mainLayout.addWidget(self.printStatus, 4, 1)
        
        self.setLayout(mainLayout)
        self.setWindowTitle("JEDI")
        self.changeStyle('Windows')

        
    def go(self):

        self.args = Arguments()

        try:
            self.args.length = int(self.textboxReqLen.text())
        except:
            self.args.length = 100000000
            
        trafficClasses = ""
        trafficRatio   = ""
        i = 0
        valid = True

        for row in self.columnAs:

            if row.checkState() == PyQt5.QtCore.Qt.Checked:

                trafficClasses += str(self.trafficClasses[i]) + ":"
                self.columnDs[i].setFlags(PyQt5.QtCore.Qt.ItemIsEditable | PyQt5.QtCore.Qt.ItemIsEnabled)

                if self.columnDs[i].text() == "" or self.columnDs[i].text().isdigit() == False:

                    self.columnDs[i].setText("enter value and press enter")
                    valid = False

                trafficRatio   += str(self.columnDs[i].text()) + ":"
            i += 1

        if valid == False:
            return
        
        if trafficClasses != "":
            trafficClasses = trafficClasses[:-1]
            trafficRatio   = trafficRatio[:-1]
        else:
            ##  setting the defaults
            trafficClasses = "v"
            trafficRatio   = "1"

        self.submitButton.setEnabled(False)
        self.submitButton.setText("Generating..")
        
        self.args.traffic_classes = trafficClasses
        self.args.traffic_ratio   = trafficRatio        


        self.thread = QThread()
        self.worker = Worker()
        self.worker.setargs(self.args)
        self.worker.setPrintBox(self.printStatus)
        self.worker.moveToThread(self.thread)
        self.thread.started.connect(self.worker.run)
        self.worker.finished.connect(self.thread.quit)
        self.worker.finished.connect(self.worker.deleteLater)
        self.thread.finished.connect(self.thread.deleteLater)

        self.thread.start()

        self.thread.finished.connect(
            lambda: self.submitButton.setEnabled(True)
        )
        self.thread.finished.connect(
            lambda: self.submitButton.setText("Generate")
        )

        print("Done!")
        
    def changeStyle(self, styleName):

        QApplication.setStyle(QStyleFactory.create(styleName))
        self.changePalette()

        
    def changePalette(self):

        if (self.useStylePaletteCheckBox.isChecked()):
            QApplication.setPalette(QApplication.style().standardPalette())
        else:
            QApplication.setPalette(self.originalPalette)

            
    def advanceProgressBar(self):

        try:
            self.progressBar.setValue(float(self.traceGenerator.curr_iter)*100/self.args.length)
        except:
            self.progressBar.setValue(0)

            
    def createHRRadioButtons(self):

        self.topLeftGroupBox = QGroupBox("1. Select hitrate type: ")

        self.radioButtonRHR = QRadioButton("Request Hitrate")
        self.radioButtonRHR.setChecked(True)

        self.radioButtonBHR = QRadioButton("Byte Hitrate")

        layout = QVBoxLayout()
        layout.addWidget(self.radioButtonRHR)
        layout.addWidget(self.radioButtonBHR)
        self.topLeftGroupBox.setLayout(layout)    
        

    def createTrafficVolumeBox(self):

        self.trafficVolumeBox = QGroupBox("2. Select traffic volume unit: ")

        self.reqRateButton = QRadioButton("Requests/second")
        self.reqRateButton.setChecked(True)
        self.reqRateButton.clicked.connect(self.updateColumnToReqrate)
        
        self.byteRateButton = QRadioButton("Gbps")
        self.byteRateButton.clicked.connect(self.updateColumnToByterate)

        layout = QHBoxLayout()
        layout.addWidget(self.reqRateButton)
        layout.addWidget(self.byteRateButton)
        self.trafficVolumeBox.setLayout(layout)    

        
    def updateColumnToReqrate(self):
        self.tableWidget.setHorizontalHeaderLabels(['Traffic class', 'Description',  'Traffic volume \n (Requests/sec)'])

    def updateColumnToByterate(self):
        self.tableWidget.setHorizontalHeaderLabels(['Traffic class', 'Description',  'Traffic volume \n (Gbps)'])
    
    def createTraceLengthTextField(self):

        self.topRightGroupBox = QGroupBox("1. Enter trace length (no. of requests) : ")
        self.textboxReqLen = QLineEdit(self)
        self.textboxReqLen.setText("100000000")
        
        layout = QVBoxLayout()
        layout.addWidget(self.textboxReqLen)
        self.topRightGroupBox.setLayout(layout)

    def readAvailableTrafficClasses(self):

        self.trafficClasses      = []
        self.trafficVolumes      = []
        self.requestRates        = []        
        self.trafficDescription  = []
        
        f = open("FOOTPRINT_DESCRIPTORS/available_fds.txt", "r")
        for l in f:
            l = l.strip().split(",")
            self.trafficClasses.append(l[1])
            self.trafficVolumes.append(l[3])
            self.requestRates.append(l[4])
            self.trafficDescription.append(l[5])
        f.close()
        
    def createTrafficClassList(self):
        self.bottomLeftTabWidget = QGroupBox("3. Select required traffic classes and specify traffic volume (hit enter after):")

        self.tableWidget = QTableWidget(len(self.trafficClasses), 3)
        checkbox = PyQt5.QtWidgets.QCheckBox()
        self.tableWidget.setHorizontalHeaderLabels(['Traffic class', 'Description',  'Traffic volume \n (Requests/sec)'])

        header = self.tableWidget.horizontalHeader()       
        header.setSectionResizeMode(0, PyQt5.QtWidgets.QHeaderView.ResizeToContents)
        header.setSectionResizeMode(1, PyQt5.QtWidgets.QHeaderView.Stretch)
        header.setSectionResizeMode(2, PyQt5.QtWidgets.QHeaderView.Stretch)
        
        self.columnAs = []
        self.columnDs = []                                          
                          
        for row_id in range(len(self.trafficClasses)):

            ## Fill column 1 with the traffic classes
            rowA = QTableWidgetItem(self.trafficClasses[row_id])
            rowA.setFlags(PyQt5.QtCore.Qt.ItemIsUserCheckable |  PyQt5.QtCore.Qt.ItemIsEnabled)
            rowA.setCheckState(PyQt5.QtCore.Qt.Unchecked)
            self.tableWidget.setItem(row_id, 0, rowA)
            self.columnAs.append(rowA)

            ## Fill column 3 with traffic type
            rowC = QTableWidgetItem(self.trafficDescription[row_id])
            rowC.setTextAlignment(Qt.AlignHCenter)
            self.tableWidget.setItem(row_id, 1, rowC)            
                        
            ## Fill column 4 with an editable field
            ## for the user to enter a value to scale the traffic by
            rowD = QTableWidgetItem("")
            rowD.setFlags(PyQt5.QtCore.Qt.ItemIsEditable | PyQt5.QtCore.Qt.ItemIsEnabled)
            rowD.setTextAlignment(Qt.AlignHCenter)
            self.tableWidget.setItem(row_id, 2, rowD)
            self.columnDs.append(rowD)
            
        layout = QHBoxLayout()
        layout.addWidget(self.tableWidget)
        self.bottomLeftTabWidget.setLayout(layout)
        

    def createProgressBar(self):

        self.progressBar = QProgressBar()
        self.progressBar.setRange(0, 100)
        self.progressBar.setValue(0)

        timer = QTimer(self)
        timer.timeout.connect(self.advanceProgressBar)
        timer.start(100)


if __name__ == '__main__':
    import sys
    app = QApplication(sys.argv)
    gallery = WidgetGallery()
    gallery.show()
    sys.exit(app.exec_()) 
