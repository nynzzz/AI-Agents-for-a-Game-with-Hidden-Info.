module sneakthrough {
    requires javafx.controls;
    requires com.opencsv;
    exports sneakthrough.GUI;
    opens sneakthrough.GUI to javafx.graphics;
    exports sneakthrough.GUI_new;
    opens sneakthrough.GUI_new to javafx.graphics;

}
