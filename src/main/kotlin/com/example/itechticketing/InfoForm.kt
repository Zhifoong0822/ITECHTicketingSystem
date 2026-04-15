package com.example.itechticketing

import java.awt.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.swing.*
import javax.swing.border.TitledBorder

class InfoForm : JFrame() {
    // 表单组件 - 按照您的字段顺序
    private val ticketNoField = JTextField(20)
    private val dateField = JFormattedTextField(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()))
    private val brandField = JTextField(20)
    private val partsNumberField = JTextField(20)
    private val dcField = JTextField(20)
    private val ticketsField = JTextField(20)
    private val leadEngineerField = JTextField(20)
    private val otherEngineerField = JTextField(20)
    private val manpowerField = JTextField(10)
    private val startTimeField = JFormattedTextField(DateTimeFormatter.ofPattern("HH:mm").format(LocalTime.now()))
    private val endTimeField = JFormattedTextField(DateTimeFormatter.ofPattern("HH:mm").format(LocalTime.now()))

    // 显示区域
    private val textArea = JTextArea(20, 60)
    private val ticketsList = mutableListOf<Map<String, String>>()

    init {
        setupUI()
    }

    private fun setupUI() {
        title = "TICKETING SYSTEM 工单信息管理系统"
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        layout = BorderLayout(10, 10)

        // 主面板
        val mainPanel = JPanel(BorderLayout(10, 10))
        mainPanel.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

        // 表单面板
        val formPanel = createFormPanel()
        mainPanel.add(formPanel, BorderLayout.CENTER)

        // 按钮面板
        val buttonPanel = createButtonPanel()
        mainPanel.add(buttonPanel, BorderLayout.SOUTH)

        add(mainPanel, BorderLayout.NORTH)

        // 显示面板
        val displayPanel = createDisplayPanel()
        add(displayPanel, BorderLayout.CENTER)

        // 设置窗口
        pack()
        setLocationRelativeTo(null)
        isVisible = true
    }

    private fun createFormPanel(): JPanel {
        val panel = JPanel(GridBagLayout())
        panel.border = TitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "TICKET INFO 工单信息录入",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            Font("Microsoft YaHei", Font.BOLD, 14)
        )

        val gbc = GridBagConstraints()
        gbc.insets = Insets(8, 8, 8, 8)
        gbc.fill = GridBagConstraints.HORIZONTAL

        // 第1行：Ticket No (Primary Key)
        gbc.gridx = 0
        gbc.gridy = 0
        gbc.weightx = 0.3
        panel.add(createLabel("Ticket No (Primary Key):"), gbc)
        gbc.gridx = 1
        gbc.weightx = 0.7
        panel.add(ticketNoField, gbc)

        // 第2行：Date
        gbc.gridx = 0
        gbc.gridy = 1
        gbc.weightx = 0.3
        panel.add(createLabel("Date:"), gbc)
        gbc.gridx = 1
        gbc.weightx = 0.7
        panel.add(dateField, gbc)

        // 第3行：Brand 品牌
        gbc.gridx = 0
        gbc.gridy = 2
        gbc.weightx = 0.3
        panel.add(createLabel("Brand 品牌:"), gbc)
        gbc.gridx = 1
        gbc.weightx = 0.7
        panel.add(brandField, gbc)

        // 第4行：Parts Number
        gbc.gridx = 0
        gbc.gridy = 3
        gbc.weightx = 0.3
        panel.add(createLabel("Parts Number:"), gbc)
        gbc.gridx = 1
        gbc.weightx = 0.7
        panel.add(partsNumberField, gbc)

        // 第5行：DC 机房
        gbc.gridx = 0
        gbc.gridy = 4
        gbc.weightx = 0.3
        panel.add(createLabel("DC 机房:"), gbc)
        gbc.gridx = 1
        gbc.weightx = 0.7
        panel.add(dcField, gbc)

        // 第6行：Tickets 工单
        gbc.gridx = 0
        gbc.gridy = 5
        gbc.weightx = 0.3
        panel.add(createLabel("Tickets 工单:"), gbc)
        gbc.gridx = 1
        gbc.weightx = 0.7
        panel.add(ticketsField, gbc)

        // 第7行：Lead Engineer
        gbc.gridx = 0
        gbc.gridy = 6
        gbc.weightx = 0.3
        panel.add(createLabel("Lead Engineer:"), gbc)
        gbc.gridx = 1
        gbc.weightx = 0.7
        panel.add(leadEngineerField, gbc)

        // 第8行：Other Engineer
        gbc.gridx = 0
        gbc.gridy = 7
        gbc.weightx = 0.3
        panel.add(createLabel("Other Engineer:"), gbc)
        gbc.gridx = 1
        gbc.weightx = 0.7
        panel.add(otherEngineerField, gbc)

        // 第9行：Total人天 Manpower
        gbc.gridx = 0
        gbc.gridy = 8
        gbc.weightx = 0.3
        panel.add(createLabel("Total Manpower 人力  :"), gbc)
        gbc.gridx = 1
        gbc.weightx = 0.7
        panel.add(manpowerField, gbc)

        // 第10行：Start Time
        gbc.gridx = 0
        gbc.gridy = 9
        gbc.weightx = 0.3
        panel.add(createLabel("Start Time:"), gbc)
        gbc.gridx = 1
        gbc.weightx = 0.7
        panel.add(startTimeField, gbc)

        // 第11行：End Time
        gbc.gridx = 0
        gbc.gridy = 10
        gbc.weightx = 0.3
        panel.add(createLabel("End Time:"), gbc)
        gbc.gridx = 1
        gbc.weightx = 0.7
        panel.add(endTimeField, gbc)

        return panel
    }

    private fun createLabel(text: String): JLabel {
        val label = JLabel(text, JLabel.RIGHT)
        label.font = Font("Microsoft YaHei", Font.PLAIN, 12)
        label.preferredSize = Dimension(150, 25)
        return label
    }

    private fun createButtonPanel(): JPanel {
        val panel = JPanel(FlowLayout(FlowLayout.CENTER, 15, 10))
        panel.border = BorderFactory.createEmptyBorder(5, 0, 5, 0)

        val submitButton = createButton("Submit 提交工单", Color(70, 130, 180))
        submitButton.addActionListener { onSubmit() }

        val clearButton = createButton("Clear Form 清空表单", Color(255, 99, 71))
        clearButton.addActionListener { onClear() }

        val deleteButton = createButton("Delete Ticket 删除工单", Color(220, 20, 60))
        deleteButton.addActionListener { onDeleteByTicketNo() }

        val exportButton = createButton("Export CSV 导出CSV", Color(34, 139, 34))
        exportButton.addActionListener { onExport() }

        panel.add(submitButton)
        panel.add(clearButton)
        panel.add(deleteButton)
        panel.add(exportButton)

        return panel
    }

    private fun createButton(text: String, color: Color): JButton {
        return JButton(text).apply {
            background = color
            foreground = color
            font = Font("Microsoft YaHei", Font.BOLD, 12)
            preferredSize = Dimension(150, 35)
            isFocusPainted = false
            isOpaque = true
            isBorderPainted = true
            setContentAreaFilled(true)
        }
    }

    private fun createDisplayPanel(): JPanel {
        val panel = JPanel(BorderLayout())
        panel.border = TitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "Ticket submitted\n已提交工单列表",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            Font("Microsoft YaHei", Font.BOLD, 12)
        )

        textArea.font = Font("Monospaced", Font.PLAIN, 12)
        textArea.isEditable = false
        textArea.background = Color(245, 245, 245)

        val scrollPane = JScrollPane(textArea)
        scrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        scrollPane.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED

        panel.add(scrollPane, BorderLayout.CENTER)

        return panel
    }

    private fun onSubmit() {
        // 验证工单号
        if (ticketNoField.text.trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Ticket No can't be empty\nTicket No 不能为空",
                "Error 验证错误",
                JOptionPane.ERROR_MESSAGE
            )
            return
        }

        // 检查是否重复
        val existingTicket = ticketsList.find { it["Ticket No"] == ticketNoField.text.trim() }
        if (existingTicket != null) {
            JOptionPane.showMessageDialog(
                this,
                "Ticket No ${ticketNoField.text.trim()} already exists\n工单号 ${ticketNoField.text.trim()} 已存在!",
                "Repeated 重复错误",
                JOptionPane.ERROR_MESSAGE
            )
            return
        }

        // 收集表单数据
        val ticketData = mapOf(
            "Ticket No" to ticketNoField.text.trim(),
            "Date" to dateField.text.trim(),
            "Brand" to brandField.text.trim(),
            "Parts Number" to partsNumberField.text.trim(),
            "DC" to dcField.text.trim(),
            "Tickets" to ticketsField.text.trim(),
            "Lead Engineer" to leadEngineerField.text.trim(),
            "Other Engineer" to otherEngineerField.text.trim(),
            "Manpower" to manpowerField.text.trim(),
            "Start Time" to startTimeField.text.trim(),
            "End Time" to endTimeField.text.trim()
        )

        ticketsList.add(ticketData)
        displayTicket(ticketData)
        onClear()

        JOptionPane.showMessageDialog(
            this,
            "Ticket submitted\n工单提交成功",
            "Done 成功",
            JOptionPane.INFORMATION_MESSAGE
        )
    }

    private fun displayTicket(ticket: Map<String, String>) {
        val separator = "=".repeat(90)
        val displayText = buildString {
            append(separator)
            append("\n")
            append("Ticket No: ${ticket["Ticket No"]}\n")
            append("Date: ${ticket["Date"]}\n")
            append("Brand 品牌: ${ticket["Brand"]}\n")
            append("Parts Number: ${ticket["Parts Number"]}\n")
            append("DC 机房: ${ticket["DC"]}\n")
            append("Tickets 工单: ${ticket["Tickets"]}\n")
            append("Lead Engineer: ${ticket["Lead Engineer"]}\n")
            append("Other Engineer: ${ticket["Other Engineer"]}\n")
            append("Total Manpower 人力: ${ticket["Manpower"]}\n")
            append("Start Time: ${ticket["Start Time"]}\n")
            append("End Time: ${ticket["End Time"]}\n")
            append(separator)
            append("\n\n")
        }

        // 添加到显示区域顶部（最新的显示在最上面）
        val currentText = textArea.text
        textArea.text = displayText + currentText
    }

    private fun onClear() {
        ticketNoField.text = ""
        dateField.text = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        brandField.text = ""
        partsNumberField.text = ""
        dcField.text = ""
        ticketsField.text = ""
        leadEngineerField.text = ""
        otherEngineerField.text = ""
        manpowerField.text = ""
        startTimeField.text = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        endTimeField.text = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))

        ticketNoField.requestFocus()
    }

    private fun onDeleteByTicketNo() {
        // 创建输入对话框
        val ticketNo = JOptionPane.showInputDialog(
            this,
            "Please enter the Ticket No to delete:\n请输入要删除的工单号:",
            "Delete Ticket 删除工单",
            JOptionPane.QUESTION_MESSAGE
        )

        // 检查用户是否取消了输入
        if (ticketNo == null) {
            return
        }

        // 检查是否输入为空
        if (ticketNo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Ticket No cannot be empty\n工单号不能为空",
                "Error 错误",
                JOptionPane.ERROR_MESSAGE
            )
            return
        }

        // 查找工单是否存在
        val ticketToDelete = ticketsList.find { it["Ticket No"] == ticketNo.trim() }

        if (ticketToDelete == null) {
            JOptionPane.showMessageDialog(
                this,
                "Ticket No '$ticketNo' not found\n工单号 '$ticketNo' 不存在",
                "Not Found 未找到",
                JOptionPane.WARNING_MESSAGE
            )
            return
        }

        // 确认删除
        val confirm = JOptionPane.showConfirmDialog(
            this,
            "Confirm delete Ticket No: ${ticketNo.trim()}?\n\n" +
                    "Brand: ${ticketToDelete["Brand"]}\n" +
                    "DC: ${ticketToDelete["DC"]}\n" +
                    "Date: ${ticketToDelete["Date"]}\n\n" +
                    "确定要删除工单号: ${ticketNo.trim()} 吗？",
            "Confirmation 确认删除",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        )

        if (confirm == JOptionPane.YES_OPTION) {
            ticketsList.removeAll { it["Ticket No"] == ticketNo.trim() }
            refreshDisplay()

            JOptionPane.showMessageDialog(
                this,
                "Ticket No: ${ticketNo.trim()} deleted successfully\n工单号: ${ticketNo.trim()} 删除成功！",
                "Success 成功",
                JOptionPane.INFORMATION_MESSAGE
            )
        }
    }

    private fun refreshDisplay() {
        textArea.text = ""
        // 按添加顺序显示
        ticketsList.forEach { ticket ->
            displayTicket(ticket)
        }
    }

    private fun onExport() {
        if (ticketsList.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "No ticket to export\n没有工单数据可导出",
                "Notice 提示",
                JOptionPane.WARNING_MESSAGE
            )
            return
        }

        val fileChooser = JFileChooser()
        fileChooser.selectedFile = java.io.File("工单数据_${System.currentTimeMillis()}.csv")

        val result = fileChooser.showSaveDialog(this)
        if (result == JFileChooser.APPROVE_OPTION) {
            val file = fileChooser.selectedFile
            try {
                file.writeText(convertToCSV())
                JOptionPane.showMessageDialog(
                    this,
                    "Export Successful!\n导出成功！\nFile saved at ${file.absolutePath}\n文件保存至：${file.absolutePath}",
                    "Exported 导出完成",
                    JOptionPane.INFORMATION_MESSAGE
                )
            } catch (e: Exception) {
                JOptionPane.showMessageDialog(
                    this,
                    "Export failed 导出失败: ${e.message}",
                    "Error 错误",
                    JOptionPane.ERROR_MESSAGE
                )
            }
        }
    }

    private fun convertToCSV(): String {
        val csvBuilder = StringBuilder()

        // CSV 表头
        csvBuilder.append("Ticket No,Date,Brand,Parts Number,DC,Tickets,Lead Engineer,Other Engineer,Manpower,Start Time,End Time\n")

        // 数据行
        for (ticket in ticketsList) {
            csvBuilder.append("\"${ticket["Ticket No"]}\",")
            csvBuilder.append("\"${ticket["Date"]}\",")
            csvBuilder.append("\"${ticket["Brand"]}\",")
            csvBuilder.append("\"${ticket["Parts Number"]}\",")
            csvBuilder.append("\"${ticket["DC"]}\",")
            csvBuilder.append("\"${ticket["Tickets"]}\",")
            csvBuilder.append("\"${ticket["Lead Engineer"]}\",")
            csvBuilder.append("\"${ticket["Other Engineer"]}\",")
            csvBuilder.append("\"${ticket["Manpower"]}\",")
            csvBuilder.append("\"${ticket["Start Time"]}\",")
            csvBuilder.append("\"${ticket["End Time"]}\"\n")
        }

        return csvBuilder.toString()
    }
}

// main 函数放在类外面
fun main() {
    // 设置系统外观
    try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    } catch (e: Exception) {
        e.printStackTrace()
    }

    // 在事件调度线程中运行UI
    SwingUtilities.invokeLater {
        InfoForm()
    }
}