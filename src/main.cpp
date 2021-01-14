#include <QApplication>

#ifndef QT_NO_SYSTEMTRAYICON

#include <QMessageBox>
#include "macchiato.h"

int main(int argc, char **argv) {
    Q_INIT_RESOURCE(macchiato);
    QCoreApplication::setAttribute(Qt::AA_UseHighDpiPixmaps);
    QApplication app(argc, argv);
    if (!QSystemTrayIcon::isSystemTrayAvailable()) {
        QMessageBox::critical(nullptr, QObject::tr("Macchiato"),
                              QObject::tr("A system tray was not detected on this system"));
        return std::make_error_code(std::errc::function_not_supported).value();
    }
    QApplication::setQuitOnLastWindowClosed(false);
    Macchiato macchiato;
    macchiato.show();
    return app.exec();
}

#else

#include <QLabel>
#include <QDebug>

int main(int argc, char** argv){
    QApplication app(argc, argv);
    QString text("QSystemTrayIcon is not supported on this platform");

    QLabel *label = new QLabel(text);
    label->setWordWrap(true);

    label->show();
    qDebug() << text;

    app.exec();
}

#endif