module sneakthrough {
    requires javafx.controls;
    exports sneakthrough.GUI;
    opens sneakthrough.GUI to javafx.graphics;
}
