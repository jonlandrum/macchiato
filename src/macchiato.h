#ifndef MACCHIATO_H
#define MACCHIATO_H

#include <QObject>

class macchiato : public QObject
{
    Q_OBJECT
public:
    explicit macchiato(QObject *parent = nullptr);

signals:

};

#endif // MACCHIATO_H
