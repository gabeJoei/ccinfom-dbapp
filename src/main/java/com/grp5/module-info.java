module com.grp5 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    opens com.grp5.controller to javafx.fxml;
    opens com.grp5.model to javafx.base;
    opens com.grp5 to javafx.graphics;
   
    exports com.grp5;
}
