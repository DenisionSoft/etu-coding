package com.example.misis2;

import com.example.misis2.ei.EI;
import com.example.misis2.ei.EIService;
import com.example.misis2.product.Product;
import com.example.misis2.product.ProductService;
import com.example.misis2.productclassifier.ProductClassifier;
import com.example.misis2.productclassifier.ProductClassifierService;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.ArrayList;

import static java.util.Arrays.copyOfRange;

class ResponseScreenSmall extends JFrame {
    public ResponseScreenSmall(String status, String message) {
        super("Ответ");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(250, 150);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        JLabel title = new JLabel(status);
        title.setBounds(100, 10, 70, 25);
        add(title);

        JLabel text = new JLabel(message);
        text.setBounds(20, 45, 200, 25);
        add(text);

        JButton button = new JButton("Закрыть");
        button.setBounds(75, 80, 100, 25);
        button.addActionListener(e -> {
            dispose();
        });

        add(button);
        setVisible(true);
    }
}

class ResponseScreenMedium extends JFrame {
    public ResponseScreenMedium(Integer id, String name, Integer base_ei, Integer parent_id) {
        super("Ответ");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 150);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        String[] colNames = {"id", "name", "base_ei", "parent_id"};
        Object[][] data = {
                {id, name, base_ei, parent_id}
        };

        JTable table = new JTable(data, colNames);
        table.setDefaultEditor(Object.class, null);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        scrollPane.setBounds(10, 10, 450, 50);
        add(scrollPane);

        JButton button = new JButton("Закрыть");
        button.setBounds(200, 80, 100, 25);
        button.addActionListener(e -> {
            dispose();
        });

        add(button);
        setVisible(true);
    }
}

class ResponseScreenLarge extends JFrame {
    public ResponseScreenLarge(ArrayList<?> data, String[] colNames) {
        super("Ответ");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 800);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        Object[][] dataArr = new Object[data.size()][colNames.length];
        for (int i = 0; i < data.size(); i++) {
            String datapoint = data.get(i).toString();
            datapoint = datapoint.replace(",)", ", )");
            datapoint = datapoint.replaceAll("[()]", "");
            datapoint = datapoint.replaceAll(",,,", ", , , ");
            dataArr[i] = datapoint.split(",");
        }

        JTable table = new JTable(dataArr, colNames);
        table.setDefaultEditor(Object.class, null);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        scrollPane.setBounds(10, 10, 950, 700);
        add(scrollPane);

        JButton button = new JButton("Закрыть");
        button.setBounds(200, 80, 100, 25);
        button.addActionListener(e -> {
            dispose();
        });

        add(button);
        setVisible(true);
    }
}

class LoginScreen extends JFrame {
    private Integer login = null;
    public LoginScreen() {
        super("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(250, 200);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("Администратор");
        comboBox.addItem("Пользователь");
        comboBox.setSelectedItem("Администратор");
        comboBox.setBounds(50, 50, 150, 20);
        add(comboBox);

        JButton button = new JButton("Войти");
        button.setBounds(75, 100, 100, 25);
        button.addActionListener(e -> {
            String loginStr = (String) comboBox.getSelectedItem();
            login = loginStr.equals("Администратор") ? 1 : 0;
        });
        add(button);

        setVisible(true);
    }

    public Integer getLogin() {
        return login;
    }

}

class InsertScreen extends JFrame {
    public InsertScreen() {
        super("Добавление");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(250, 150);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("Создать");
        title.setBounds(100, 10, 70, 25);
        add(title);

        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("Базовая ЕИ");
        comboBox.addItem("Класс изделия");
        comboBox.addItem("Новый корень");
        comboBox.addItem("Изделие");
        comboBox.setSelectedItem("Базовая ЕИ");
        comboBox.setBounds(20, 45, 200, 25);
        add(comboBox);

        JButton button = new JButton("Далее");
        button.setBounds(75, 80, 100, 25);
        button.addActionListener(e -> {
            dataScreen((String) comboBox.getSelectedItem());
            dispose();
        });

        add(button);
        setVisible(true);
    }

    public void dataScreen(String type) {

        JFrame dataScreenFrame = new JFrame("Создание");
        dataScreenFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dataScreenFrame.setSize(250, 260);
        dataScreenFrame.setLayout(null);
        dataScreenFrame.setResizable(false);
        dataScreenFrame.setLocationRelativeTo(null);

        JLabel title = new JLabel(type + ", введите данные");
        title.setBounds(20, 10, 200, 25);
        dataScreenFrame.add(title);

        JLabel nameLabel = new JLabel("Название");
        nameLabel.setBounds(10, 45, 100, 25);
        dataScreenFrame.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(110, 45, 120, 25);
        dataScreenFrame.add(nameField);

        JLabel baseeiLabel = new JLabel("Базовая ЕИ");
        baseeiLabel.setBounds(10, 80, 100, 25);
        baseeiLabel.setEnabled(type == "Класс изделия" || type == "Новый корень");
        dataScreenFrame.add(baseeiLabel);

        JSpinner baseeiSpinner = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        baseeiSpinner.setBounds(110, 80, 120, 25);
        baseeiSpinner.setEnabled(type == "Класс изделия" || type == "Новый корень");
        dataScreenFrame.add(baseeiSpinner);

        JLabel classLabel = new JLabel("Класс-родитель");
        classLabel.setBounds(10, 150, 100, 25);
        classLabel.setEnabled(type == "Класс изделия" || type == "Изделие");
        dataScreenFrame.add(classLabel);

        JSpinner classSpinner = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        classSpinner.setBounds(110, 150, 120, 25);
        classSpinner.setEnabled(type == "Класс изделия" || type == "Изделие");
        dataScreenFrame.add(classSpinner);

        JCheckBox nullBox = new JCheckBox("Без родителя");
        nullBox.setBounds(10, 115, 120, 25);
        nullBox.setEnabled(type == "Класс изделия");
        nullBox.addActionListener(e -> {
            if (nullBox.isSelected()) {
                classSpinner.setEnabled(false);
                classLabel.setEnabled(false);
            } else {
                classSpinner.setEnabled(true);
                classLabel.setEnabled(true);
            }
        });
        dataScreenFrame.add(nullBox);

        JButton button = new JButton("Создать");
        button.setBounds(75, 185, 100, 25);
        button.addActionListener(e -> {
            String r_name = nameField.getText();
            Integer r_base_ei = (Integer) baseeiSpinner.getValue();
            Integer r_parent_id = (Integer) classSpinner.getValue();
            Integer response;
            switch (type) {
                case "Базовая ЕИ":
                    response = EIService.ins_ei(r_name);
                    if (response == 1)
                        new ResponseScreenSmall("Успех", "");
                    else
                        new ResponseScreenSmall("Ошибка", r_name + " уже существует");
                    break;
                case "Класс изделия":
                    if (nullBox.isSelected()) { r_parent_id = null; }
                    response = ProductClassifierService.ins_pc(r_name, r_base_ei, r_parent_id);
                    if (response == 1)
                        new ResponseScreenSmall("Успех", "");
                    else
                        new ResponseScreenSmall("Ошибка", "");
                    break;
                case "Новый корень":
                    response = ProductClassifierService.ins_pc_root(r_name, r_base_ei);
                    if (response == 1)
                        new ResponseScreenSmall("Успех", "");
                    else
                        new ResponseScreenSmall("Ошибка", "");
                    break;
                case "Изделие":
                    response = ProductService.ins_product(r_name, r_parent_id);
                    if (response == 1)
                        new ResponseScreenSmall("Успех", "");
                    else if (response == -1)
                        new ResponseScreenSmall("Ошибка", "Класса с id " + r_parent_id + " не существует");
                    else
                        new ResponseScreenSmall("Ошибка", "Изделие " + r_name + " уже существует");
                    break;
            }
        });
        dataScreenFrame.add(button);

        dataScreenFrame.setVisible(true);
    }
}

class DeleteScreen extends JFrame {
    public DeleteScreen() {
        super("Удаление");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(250, 200);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("Удалить");
        title.setBounds(100, 10, 70, 25);
        add(title);

        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("Базовая ЕИ");
        comboBox.addItem("Класс изделия");
        comboBox.addItem("Изделие");
        comboBox.setSelectedItem("Базовая ЕИ");
        comboBox.setBounds(20, 45, 200, 25);
        add(comboBox);

        JLabel idLabel = new JLabel("id");
        idLabel.setBounds(30, 80, 20, 25);
        add(idLabel);

        JSpinner idSpinner = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        idSpinner.setBounds(60, 80, 120, 25);
        add(idSpinner);

        JButton button = new JButton("Удалить");
        button.setBounds(75, 115, 100, 25);
        button.addActionListener(e -> {
            Integer id = (Integer) idSpinner.getValue();
            Integer response = null;
            switch (comboBox.getSelectedItem().toString()) {
                case "Базовая ЕИ":
                    response = EIService.del_ei(id);
                    if (response == 1)
                        new ResponseScreenSmall("Успех", "");
                    else
                        new ResponseScreenSmall("Ошибка", "ЕИ не существует или используется");
                    break;
                case "Класс изделия":
                    response = ProductClassifierService.del_pc(id);
                    if (response == 1)
                        new ResponseScreenSmall("Успех", "");
                    else
                        new ResponseScreenSmall("Ошибка", "Класс не существует");
                    break;
                case "Изделие":
                    response = ProductService.del_product(id);
                    if (response == 1)
                        new ResponseScreenSmall("Успех", "");
                    else
                        new ResponseScreenSmall("Ошибка", "Изделие не существует");
                    break;
            }
        });
        add(button);

        setVisible(true);
    }
}

class UpdateScreen extends JFrame {
    public UpdateScreen() {
        super("Редактирование");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(250, 220);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("Редактировать");
        title.setBounds(65, 10, 140, 25);
        add(title);

        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("Базовая ЕИ");
        comboBox.addItem("Класс изделия");
        comboBox.addItem("Изделие");
        comboBox.setSelectedItem("Базовая ЕИ");
        comboBox.setBounds(20, 45, 200, 25);
        add(comboBox);

        JLabel idLabel = new JLabel("id");
        idLabel.setBounds(30, 80, 60, 25);
        add(idLabel);

        JLabel nameLabel = new JLabel("Имя");
        nameLabel.setBounds(100, 80, 60, 25);
        add(nameLabel);

        JSpinner idSpinner = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        idSpinner.setBounds(30, 110, 60, 25);
        add(idSpinner);

        JTextField nameField = new JTextField();
        nameField.setBounds(100, 110, 110, 25);
        add(nameField);

        JButton button = new JButton("Задать");
        button.setBounds(65, 150, 100, 25);
        button.addActionListener(e -> {
            Integer id = (Integer) idSpinner.getValue();
            String name = nameField.getText();
            Integer response = null;
            switch (comboBox.getSelectedItem().toString()) {
                case "Базовая ЕИ":
                    response = EIService.update_ei(id, name);
                    if (response == 1)
                        new ResponseScreenSmall("Успех", "");
                    else if (response == -1)
                        new ResponseScreenSmall("Ошибка", "Имя " + name + " уже существует");
                    else
                        new ResponseScreenSmall("Ошибка", "ЕИ не существует");
                    break;
                case "Класс изделия":
                    response = ProductClassifierService.update_pc_name(id, name);
                    if (response == 1)
                        new ResponseScreenSmall("Успех", "");
                    else if (response == -1)
                        new ResponseScreenSmall("Ошибка", "Имя " + name + " уже существует");
                    else
                        new ResponseScreenSmall("Ошибка", "Класс не существует");
                    break;
                case "Изделие":
                    response = ProductService.update_product_name(id, name);
                    if (response == 1)
                        new ResponseScreenSmall("Успех", "");
                    else if (response == -1)
                        new ResponseScreenSmall("Ошибка", "Имя " + name + " уже существует");
                    else
                        new ResponseScreenSmall("Ошибка", "Изделие не существует");
                    break;
            }
        });
        add(button);

        setVisible(true);
    }
}

class changeparentScreen extends JFrame {
    public changeparentScreen() {
        super("Изменить родителя");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(250, 220);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("Изменить родителя");
        title.setBounds(50, 10, 140, 25);
        add(title);

        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("Класс изделия");
        comboBox.addItem("Изделие");
        comboBox.setSelectedItem("Класс изделия");
        comboBox.setBounds(20, 45, 200, 25);
        add(comboBox);

        JLabel idLabel = new JLabel("id");
        idLabel.setBounds(20, 80, 60, 25);
        add(idLabel);

        JLabel parentLabel = new JLabel("Родитель");
        parentLabel.setBounds(20, 115, 60, 25);
        add(parentLabel);

        JSpinner idSpinner = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        idSpinner.setBounds(90, 80, 120, 25);
        add(idSpinner);

        JSpinner parentSpinner = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        parentSpinner.setBounds(90, 115, 120, 25);
        add(parentSpinner);

        JButton button = new JButton("Задать");
        button.setBounds(65, 150, 100, 25);
        button.addActionListener(e -> {
            Integer id = (Integer) idSpinner.getValue();
            Integer parent = (Integer) parentSpinner.getValue();
            Integer response = null;
            switch (comboBox.getSelectedItem().toString()) {
                case "Класс изделия":
                    response = ProductClassifierService.update_pc_parent(id, parent);
                    if (response == 1)
                        new ResponseScreenSmall("Успех", "");
                    else
                        new ResponseScreenSmall("Ошибка", "");
                    break;
                case "Изделие":
                    response = ProductService.update_product_parent(id, parent);
                    if (response == 1)
                        new ResponseScreenSmall("Успех", "");
                    else if (response == -1)
                        new ResponseScreenSmall("Ошибка", "Родитель не существует");
                    else
                        new ResponseScreenSmall("Ошибка", "Изделие не существует");
                    break;
            }
        });
        add(button);

        setVisible(true);
    }
}

class setbaseeiScreen extends JFrame {
    public setbaseeiScreen() {
        super("Задать ЕИ");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(250, 220);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("Задать базовую ЕИ");
        title.setBounds(60, 10, 140, 25);
        add(title);

        JLabel idLabel = new JLabel("Класс");
        idLabel.setBounds(20, 80, 60, 25);
        add(idLabel);

        JLabel baseeiLabel = new JLabel("ЕИ");
        baseeiLabel.setBounds(20, 115, 60, 25);
        add(baseeiLabel);

        JSpinner idSpinner = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        idSpinner.setBounds(90, 80, 120, 25);
        add(idSpinner);

        JSpinner baseeiSpinner = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        baseeiSpinner.setBounds(90, 115, 120, 25);
        add(baseeiSpinner);

        JButton button = new JButton("Задать");
        button.setBounds(65, 150, 100, 25);
        button.addActionListener(e -> {
            Integer id = (Integer) idSpinner.getValue();
            Integer baseei = (Integer) baseeiSpinner.getValue();
            Integer response = ProductClassifierService.update_pc_base_ei(id, baseei);
            if (response == 1)
                new ResponseScreenSmall("Успех", "");
            else
                new ResponseScreenSmall("Ошибка", "ЕИ или класс не существует");
        });
        add(button);

        setVisible(true);
    }
}

class SelectScreen extends JFrame {
    public SelectScreen() {
        super("Выбрать");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(250, 220);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("Выбрать");
        title.setBounds(80, 10, 140, 25);
        add(title);

        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("Базовая ЕИ");
        comboBox.addItem("Класс изделия");
        comboBox.addItem("Изделие");
        comboBox.addItem("Родитель класса");
        comboBox.addItem("Родитель изделия");
        comboBox.setSelectedItem("Класс изделия");
        comboBox.setBounds(20, 45, 200, 25);
        add(comboBox);

        JLabel idLabel = new JLabel("id");
        idLabel.setBounds(20, 80, 60, 25);
        add(idLabel);

        JSpinner idSpinner = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        idSpinner.setBounds(90, 80, 120, 25);
        add(idSpinner);

        JButton button = new JButton("Выбрать");
        button.setBounds(65, 150, 100, 25);
        button.addActionListener(e -> {
            Integer id = (Integer) idSpinner.getValue();
            switch (comboBox.getSelectedItem().toString()) {
                case "Базовая ЕИ":
                    EI ei = EIService.select_ei(id);
                    if (ei.getId() == null) {
                        new ResponseScreenSmall("Ошибка", "ЕИ не существует");
                        break;
                    }
                    new ResponseScreenMedium(ei.getId(), ei.getName(), null, null);
                    break;
                case "Класс изделия":
                    ProductClassifier pc = ProductClassifierService.select_pc(id);
                    if (pc.getBaseEI() == null) {
                        new ResponseScreenSmall("Ошибка", "Класс не существует");
                        break;
                    }
                    new ResponseScreenMedium(pc.getId(), pc.getName(), pc.getBaseEI(), pc.getParentId());
                    break;
                case "Изделие":
                    Product product = ProductService.select_product(id);
                    if (product.getId() == null) {
                        new ResponseScreenSmall("Ошибка", "Изделие не существует");
                        break;
                    }
                    new ResponseScreenMedium(product.getId(), product.getName(), null, product.getIdClass());
                    break;
                case "Родитель класса":
                    ProductClassifier pc_parent = ProductClassifierService.select_pc_parent(id);
                    if (pc_parent.getId() == null) {
                        new ResponseScreenSmall("Ошибка", "Класс не имеет родителя");
                        break;
                    }
                    new ResponseScreenMedium(pc_parent.getId(), pc_parent.getName(), pc_parent.getBaseEI(), pc_parent.getParentId());
                    break;
                case "Родитель изделия":
                    ProductClassifier product_parent = ProductService.select_product_parent(id);
                    if (product_parent.getId() == null) {
                        new ResponseScreenSmall("Ошибка", "Изделие не существует");
                        break;
                    }
                    new ResponseScreenMedium(product_parent.getId(), product_parent.getName(), product_parent.getBaseEI(), product_parent.getParentId());
                    break;
            }
        });
        add(button);

        setVisible(true);
    }
}

class SearchScreen extends JFrame {
    public SearchScreen() {
        super("Поиск");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(250, 220);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("Поиск");
        title.setBounds(80, 10, 140, 25);
        add(title);

        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("Потомки класса");
        comboBox.addItem("Потомки класса с изделиями");
        comboBox.addItem("Изделия класса");
        comboBox.addItem("Родителей класса");
        comboBox.addItem("Родителей изделия");
        comboBox.setSelectedItem("Изделия класса");
        comboBox.setBounds(20, 45, 200, 25);
        add(comboBox);

        JLabel idLabel = new JLabel("id");
        idLabel.setBounds(20, 80, 60, 25);
        add(idLabel);

        JSpinner idSpinner = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        idSpinner.setBounds(90, 80, 120, 25);
        add(idSpinner);

        JButton button = new JButton("Далее");
        button.setBounds(65, 150, 100, 25);
        button.addActionListener(e -> {
            Integer id = (Integer) idSpinner.getValue();
            String[] columns = {"id_class", "name", "base_ei", "parent_id", "id_product", "name_product", "id_class"};
            switch (comboBox.getSelectedItem().toString()) {
                case "Потомки класса":
                    sortScreen(id, "Потомки класса", copyOfRange(columns, 0, 4));
                    break;
                case "Потомки класса с изделиями":
                    sortScreen(id, "Потомки класса с изделиями", copyOfRange(columns, 0, 7));
                    break;
                case "Изделия класса":
                    sortScreen(id, "Изделия класса", copyOfRange(columns, 4, 7));
                    break;
                case "Родителей класса":
                    sortScreen(id, "Родителей класса", copyOfRange(columns, 0, 4));
                    break;
                case "Родителей изделия":
                    sortScreen(id, "Родителей изделия", copyOfRange(columns, 0, 4));
                    break;
            }
        });
        add(button);

        setVisible(true);
    }

    public void sortScreen(Integer id, String item, String[] columns) {
        JFrame sortScreenFrame = new JFrame("Сортировка");
        sortScreenFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        sortScreenFrame.setSize(250, 220);
        sortScreenFrame.setLayout(null);
        sortScreenFrame.setResizable(false);
        sortScreenFrame.setLocationRelativeTo(null);

        JLabel title = new JLabel("Выберите поле для сортировки");
        title.setBounds(20, 10, 200, 25);
        sortScreenFrame.add(title);

        JComboBox<String> comboBox = new JComboBox<>();
        for (String column : columns) {
            comboBox.addItem(column);
        }
        comboBox.setSelectedItem(columns[0]);
        comboBox.setBounds(20, 45, 200, 25);
        sortScreenFrame.add(comboBox);

        JButton button = new JButton("Поиск");
        button.setBounds(65, 150, 100, 25);
        button.addActionListener(e -> {
            String column = comboBox.getSelectedItem().toString();
            switch (item) {
                case "Потомки класса": {
                    ArrayList<?> response = ProductClassifierService.select_pc_children(id, column);
                    if (response.size() == 0) {
                        new ResponseScreenSmall("Ошибка", "");
                        break;
                    }
                    new ResponseScreenLarge(response, columns);
                    break;
                }
                case "Потомки класса с изделиями": {
                    ArrayList<?> response = ProductClassifierService.select_pc_children_products(id, column);
                    if (response.size() == 0) {
                        new ResponseScreenSmall("Ошибка", "");
                        break;
                    }
                    new ResponseScreenLarge(response, columns);
                    break;
                }
                case "Изделия класса": {
                    ArrayList<?> response = ProductClassifierService.select_pc_children_only_products(id, column);
                    if (response.size() == 0) {
                        new ResponseScreenSmall("Ошибка", "");
                        break;
                    }
                    new ResponseScreenLarge(response, columns);
                    break;
                }
                case "Родителей класса": {
                    ArrayList<?> response = ProductClassifierService.select_pc_parents(id, column);
                    if (response.size() == 0) {
                        new ResponseScreenSmall("Ошибка", "");
                        break;
                    }
                    new ResponseScreenLarge(response, columns);
                    break;
                }
                case "Родителей изделия": {
                    ArrayList<?> response = ProductService.select_product_parents(id, column);
                    if (response.size() == 0) {
                        new ResponseScreenSmall("Ошибка", "");
                        break;
                    }
                    new ResponseScreenLarge(response, columns);
                    break;
                }
            }
        });
        sortScreenFrame.add(button);

        sortScreenFrame.setVisible(true);
    }
}

class DemoScreen extends JFrame {
    public DemoScreen() {
        setTitle("Сброс");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(190, 90);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        JButton resetButton = new JButton("Сбросить схему");
        resetButton.setBounds(10, 10, 150, 25);
        resetButton.addActionListener(e -> {
            ProductClassifierService.reset_tables();
            new ResponseScreenSmall("Успешно", "Схема сброшена");
            dispose();
        });
        add(resetButton);

        setVisible(true);
    }
}

@Component
public class GUI extends JFrame {

    public GUI() {
        LoginScreen loginScreen = new LoginScreen();
        Integer login = null;
        while (login == null) {
            try {
                login = loginScreen.getLogin();
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        loginScreen.dispose();

        boolean isAdmin = login == 1;

        setTitle("Схема");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(235, 405);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        JLabel viewLabel = new JLabel("Представление схемы");
        viewLabel.setBounds(10, 10, 200, 25);
        add(viewLabel);

        JButton selectButton = new JButton("Выбрать");
        selectButton.setBounds(10, 45, 200, 25);
        selectButton.addActionListener(e -> new SelectScreen());
        add(selectButton);

        JButton searchButton = new JButton("Найти");
        searchButton.setBounds(10, 80, 200, 25);
        searchButton.addActionListener(e -> new SearchScreen());
        add(searchButton);

        JLabel descLabel = new JLabel("Описание схемы");
        descLabel.setBounds(10, 125, 200, 25);
        add(descLabel);

        JButton addButton = new JButton("Добавить");
        addButton.setBounds(10, 160, 200, 25);
        addButton.setEnabled(isAdmin);
        addButton.addActionListener(e -> new InsertScreen());
        add(addButton);

        JButton deleteButton = new JButton("Удалить");
        deleteButton.setBounds(10, 195, 200, 25);
        deleteButton.setEnabled(isAdmin);
        deleteButton.addActionListener(e -> new DeleteScreen());
        add(deleteButton);

        JButton editButton = new JButton("Редактировать");
        editButton.setBounds(10, 230, 200, 25);
        editButton.setEnabled(isAdmin);
        editButton.addActionListener(e -> new UpdateScreen());
        add(editButton);

        JButton changeparentButton = new JButton("Изменить родителя");
        changeparentButton.setBounds(10, 265, 200, 25);
        changeparentButton.setEnabled(isAdmin);
        changeparentButton.addActionListener(e -> new changeparentScreen());
        add(changeparentButton);

        JButton setbaseeiButton = new JButton("Указать базовую ЕИ");
        setbaseeiButton.setBounds(10, 300, 200, 25);
        setbaseeiButton.setEnabled(isAdmin);
        setbaseeiButton.addActionListener(e -> new setbaseeiScreen());
        add(setbaseeiButton);

        JButton demoButton = new JButton("Сброс");
        demoButton.setBounds(10, 335, 200, 25);
        demoButton.setEnabled(isAdmin);
        demoButton.addActionListener(e -> new DemoScreen());
        add(demoButton);

        setVisible(true);
    }

    public static void main(String[] args) {
        new GUI();
    }
}
