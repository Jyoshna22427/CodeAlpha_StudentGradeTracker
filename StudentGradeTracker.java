import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class Student {
    private final String name;
    private final double grade;

    public Student(String name, double grade) {
        this.name = name;
        this.grade = grade;
    }

    public String getName() { return name; }
    public double getGrade() { return grade; }
}

public class StudentGradeTracker extends JFrame {
    private final ArrayList<Student> studentList = new ArrayList<>();

    private final JTextField nameInput;
    private final JTextField gradeInput;
    private final JTextArea reportArea;
    private final JLabel averageLabel;
    private final JLabel highestLabel;
    private final JLabel lowestLabel;

    public StudentGradeTracker() {
        setTitle("Advanced Student Grade Tracker");
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Student Data"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel("Student Name:");
        nameInput = new JTextField(12);
        JLabel gradeLabel = new JLabel("Grade (0-100):");
        gradeInput = new JTextField(5);
        
        JButton addButton = new JButton("Add Record");
        JButton clearButton = new JButton("Reset All");

        addButton.setBackground(new Color(46, 139, 87));
        addButton.setForeground(Color.WHITE);
        addButton.setOpaque(true);
        addButton.setBorderPainted(false);

        clearButton.setBackground(new Color(178, 34, 34));
        clearButton.setForeground(Color.WHITE);
        clearButton.setOpaque(true);
        clearButton.setBorderPainted(false);

        gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(nameLabel, gbc);
        gbc.gridx = 1; inputPanel.add(nameInput, gbc);
        gbc.gridx = 0; gbc.gridy = 1; inputPanel.add(gradeLabel, gbc);
        gbc.gridx = 1; inputPanel.add(gradeInput, gbc);
        gbc.gridx = 0; gbc.gridy = 2; inputPanel.add(clearButton, gbc);
        gbc.gridx = 1; inputPanel.add(addButton, gbc);

        JPanel reportPanel = new JPanel(new BorderLayout(5, 5));
        reportPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        JLabel reportTitle = new JLabel("Student Summary Report:");
        reportTitle.setFont(new Font("Arial", Font.BOLD, 12));
        
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(reportArea);
        
        reportPanel.add(reportTitle, BorderLayout.NORTH);
        reportPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel summaryPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Calculated Analytics"));
        
        averageLabel = new JLabel("Average Score: 0.00");
        highestLabel = new JLabel("Highest Score: N/A");
        lowestLabel = new JLabel("Lowest Score: N/A");
        
        Font boldFont = new Font("Arial", Font.BOLD, 13);
        averageLabel.setFont(boldFont);
        highestLabel.setFont(boldFont);
        lowestLabel.setFont(boldFont);

        summaryPanel.add(averageLabel);
        summaryPanel.add(highestLabel);
        summaryPanel.add(lowestLabel);

        add(inputPanel, BorderLayout.NORTH);
        add(reportPanel, BorderLayout.CENTER);
        add(summaryPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> handleAddStudent());
        gradeInput.addActionListener(e -> handleAddStudent());
        clearButton.addActionListener(e -> handleReset());
    }

    private void handleAddStudent() {
        String name = nameInput.getText().trim();
        String gradeStr = gradeInput.getText().trim();
        
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a student name.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (gradeStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a grade.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double grade = Double.parseDouble(gradeStr);
            
            if (grade < 0 || grade > 100) {
                JOptionPane.showMessageDialog(this, "Please enter a grade between 0 and 100.", "Invalid Grade", JOptionPane.WARNING_MESSAGE);
                return;
            }

            studentList.add(new Student(name, grade));
            
            nameInput.setText("");
            gradeInput.setText("");
            nameInput.requestFocus();
            
            updateSummaryReport();
            calculateAnalytics();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric value for the grade.", "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSummaryReport() {
        if (studentList.isEmpty()) {
            reportArea.setText("");
            return;
        }

        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append(String.format("%-25s %-10s\n", "STUDENT NAME", "GRADE"));
        reportBuilder.append("---------------------------------------\n");
        
        for (Student student : studentList) {
            reportBuilder.append(String.format("%-25s %-10.2f\n", student.getName(), student.getGrade()));
        }
        reportArea.setText(reportBuilder.toString());
    }

    private void calculateAnalytics() {
        if (studentList.isEmpty()) {
            averageLabel.setText("Average Score: 0.00");
            highestLabel.setText("Highest Score: N/A");
            lowestLabel.setText("Lowest Score: N/A");
            return;
        }

        double sum = 0;
        double highest = studentList.get(0).getGrade();
        double lowest = studentList.get(0).getGrade();
        String highestStudent = studentList.get(0).getName();
        String lowestStudent = studentList.get(0).getName();

        for (Student student : studentList) {
            double grade = student.getGrade();
            sum += grade;
            
            if (grade > highest) {
                highest = grade;
                highestStudent = student.getName();
            }
            if (grade < lowest) {
                lowest = grade;
                lowestStudent = student.getName();
            }
        }

        double average = sum / studentList.size();

        averageLabel.setText(String.format("Average Score: %.2f", average));
        highestLabel.setText(String.format("Highest Score: %.2f (%s)", highest, highestStudent));
        lowestLabel.setText(String.format("Lowest Score: %.2f (%s)", lowest, lowestStudent));
    }

    private void handleReset() {
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to clear all student records?", 
                "Confirm Reset", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            studentList.clear();
            updateSummaryReport();
            calculateAnalytics();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            new StudentGradeTracker().setVisible(true);
        });
    }
}