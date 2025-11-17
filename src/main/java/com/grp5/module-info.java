module com.grp5 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.grp5.controller_gui to javafx.fxml, javafx.graphics;
    opens com.grp5.model to javafx.base;
    opens com.grp5 to javafx.graphics;
    opens com.grp5.controller_backend to javafx.fxml;
    opens com.grp5.reports to javafx.base; 

    exports com.grp5;
    exports com.grp5.model;
    exports com.grp5.controller_gui;
}
