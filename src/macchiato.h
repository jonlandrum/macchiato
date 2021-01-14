#ifndef MACCHIATO_H
#define MACCHIATO_H

#include <QDialog>
#include <QMenu>
#include <QSystemTrayIcon>

QT_BEGIN_NAMESPACE
class QAction;

class QCheckBox;

class QComboBox;

class QGroupBox;

class QLabel;

class QLineEdit;

class QMenu;

class QPushButton;

class QSpinBox;

class QTextEdit;

QT_END_NAMESPACE

class Macchiato : public QDialog {
Q_OBJECT

public:
    Macchiato();

    ~Macchiato() override;

protected:
    void closeEvent(QCloseEvent *event) override;

private slots:

    void iconActivated(QSystemTrayIcon::ActivationReason reason);

    void showMessage();

    void messageClicked();

private:
    void createActions();

    void createTrayIcon();

    QLineEdit *titleEdit = nullptr;
    QTextEdit *bodyEdit = nullptr;
    QPushButton *showMessageButton = nullptr;

    QAction *minimizeAction = nullptr;
    QAction *maximizeAction = nullptr;
    QAction *restoreAction = nullptr;
    QAction *quitAction = nullptr;

    QSystemTrayIcon *trayIcon = nullptr;
    QMenu *trayIconMenu = nullptr;
};

#endif // MACCHIATO_H
