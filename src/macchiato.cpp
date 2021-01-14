#include "macchiato.h"

#ifndef QT_NO_SYSTEMTRAYICON

#include <QAction>
#include <QCheckBox>
#include <QComboBox>
#include <QCoreApplication>
#include <QCloseEvent>
#include <QGroupBox>
#include <QLabel>
#include <QLineEdit>
#include <QMenu>
#include <QPushButton>
#include <QSpinBox>
#include <QTextEdit>
#include <QVBoxLayout>
#include <QMessageBox>

Macchiato::Macchiato() {
    createActions();
    createTrayIcon();

//    connect(trayIcon, &QSystemTrayIcon::messageClicked, this, &Macchiato::messageClicked);
    trayIcon->setToolTip("Macchiato");
    trayIcon->show();
}

Macchiato::~Macchiato() {
    delete (minimizeAction);
    delete (maximizeAction);
    delete (restoreAction);
    delete (quitAction);

    delete (trayIconMenu);
    delete (trayIcon);
}

// -- Protected Methods ------------------------------------------------------------------------------------------------
void Macchiato::closeEvent(QCloseEvent *event) {
#ifdef Q_OS_MACOS
    if (!event->spontaneous() || !isVisible()) {
        return;
    }
#endif
    if (trayIcon->isVisible()) {
        QMessageBox::information(this, tr("Macchiato"),
                                 tr("The program will keep running in the system tray. To terminate the "
                                    "program, choose <b>Quit</b> in the context menu of the system tray entry."));
        hide();
        event->ignore();
    }
}

// -- Private Slots ----------------------------------------------------------------------------------------------------


// -- Private Methods --------------------------------------------------------------------------------------------------

void Macchiato::createActions() {
    minimizeAction = new QAction(tr("Mi&nimize"), this);
    connect(minimizeAction, &QAction::triggered, this, &QWidget::hide);

    maximizeAction = new QAction(tr("Ma&ximize"), this);
    connect(maximizeAction, &QAction::triggered, this, &QWidget::showMaximized);

    restoreAction = new QAction(tr("&Restore"), this);
    connect(restoreAction, &QAction::triggered, this, &QWidget::showNormal);

    quitAction = new QAction(tr("&Quit"), this);
    connect(quitAction, &QAction::triggered, qApp, &QCoreApplication::quit);
}

void Macchiato::createTrayIcon() {
    trayIconMenu = new QMenu(this);
    trayIconMenu->addAction(minimizeAction);
    trayIconMenu->addAction(maximizeAction);
    trayIconMenu->addAction(restoreAction);
    trayIconMenu->addSeparator();
    trayIconMenu->addAction(quitAction);

    trayIcon = new QSystemTrayIcon(QIcon(":/resources/emoji_u2615.png"), this);
    trayIcon->setContextMenu(trayIconMenu);
}

#endif