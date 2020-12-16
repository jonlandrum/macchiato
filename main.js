const { app } = require('electron')

if (handleSquirrelEvent()) {
    // squirrel event handled and app will exit in 1000ms, so don't do anything else
    return;
}

function handleSquirrelEvent() {
    if (process.argv.length === 1) {
        return false;
    }

    const ChildProcess = require('child_process');
    const path = require('path');

    const appFolder = path.resolve(process.execPath, '..');
    const rootAtomFolder = path.resolve(appFolder, '..');
    const updateDotExe = path.resolve(path.join(rootAtomFolder, 'Update.exe'));
    const exeName = path.basename(process.execPath);

    const spawn = function (command, args) {
        let spawnedProcess, error;

        try {
            spawnedProcess = ChildProcess.spawn(command, args, { detached: true });
        } catch (error) { }

        return spawnedProcess;
    };

    const spawnUpdate = function (args) {
        return spawn(updateDotExe, args);
    };

    const squirrelEvent = process.argv[1];
    switch (squirrelEvent) {
        case '--squirrel-install':
        case '--squirrel-updated':
            // Optionally do things such as:
            // - Add your .exe to the PATH
            // - Write to the registry for things like file associations and
            //   explorer context menus

            // Install desktop and start menu shortcuts
            spawnUpdate(['--createShortcut', exeName]);

            setTimeout(app.quit, 1000);
            return true;

        case '--squirrel-uninstall':
            // Undo anything you did in the --squirrel-install and
            // --squirrel-updated handlers

            // Remove desktop and start menu shortcuts
            spawnUpdate(['--removeShortcut', exeName]);

            setTimeout(app.quit, 1000);
            return true;

        case '--squirrel-obsolete':
            // This is called on the outgoing version of your app before
            // we update to the new version - it's the opposite of
            // --squirrel-updated

            app.quit();
            return true;
    }
};

const { Menu, Tray } = require('electron')
const Store = require('electron-store')
const AutoLaunch = require('auto-launch')
const Worker = require("tiny-worker")
const path = require('path')
const constants = require(path.join(__dirname, 'constants.js'))
const store = new Store(constants.SCHEMA)

let tray = null
let worker = null
app.whenReady().then(() => {
    // System tray settings
    console.log(path.join(__dirname, 'nuvola-relax.png'))
    tray = new Tray(path.join(__dirname, 'nuvola-relax.png'))
    const contextMenu = Menu.buildFromTemplate([
        {
            label: 'Macchiato',
            enabled: false
        },
        {
            label: 'Options', submenu: [
                {
                    label: 'Auto Start',
                    toolTip: 'Start Macchiato automatically when the user logs in',
                    type: 'checkbox',
                    checked: store.get(constants.AUTO_START),
                    click: function (event) {
                        if (event.checked) {
                            macchiatoLauncher.enable()
                        } else {
                            macchiatoLauncher.disable()
                        }
                        store.set(constants.AUTO_START, event.checked)
                    }
                },
                {
                    label: 'Move mouse',
                    toolTip: 'Prevent computer sleep by occasionally making a slight mouse move',
                    type: 'radio',
                    checked: store.get(constants.METHOD) === constants.MOUSE_MOVE,
                    click: function () {
                        store.set(constants.METHOD, constants.MOUSE_MOVE)
                        worker.postMessage(store.get(constants.METHOD))
                    }
                },
                {
                    label: 'Key press (F15)',
                    toolTip: 'Prevent computer sleep by occasionally simulating a key press event',
                    type: 'radio',
                    checked: store.get(constants.METHOD) === constants.KEY_PRESS,
                    click: function () {
                        store.set(constants.METHOD, constants.KEY_PRESS)
                        worker.postMessage(store.get(constants.METHOD))
                    }
                }
            ]
        },
        {
            type: 'separator'
        },
        {
            role: 'quit'
        }
    ])
    tray.setToolTip('Macchiato')
    tray.setContextMenu(contextMenu)
    tray.on('right-click', () => {
        tray.popUpContextMenu()
    })
    tray.on('click', () => {
        tray.popUpContextMenu()
    })

    // Background worker
    worker = new Worker(path.join(__dirname, "loop.js"))
    worker.onmessage = function (value) {
        console.log('response: ' + value.data)
        // worker.terminate()
    }
    if (!store.has(constants.METHOD)) {
        store.set(constants.METHOD, constants.KEY_PRESS)
    }
    setInterval(doWork, 60000)

    // Auto launcher
    var macchiatoLauncher = new AutoLaunch({
        name: 'macchiato',
        isHidden: true,
        path: app.getPath('exe')
    })
    macchiatoLauncher.isEnabled().then((isEnabled) => {
        if (isEnabled) {
            return
        }
        macchiatoLauncher.enable()
    }).catch((err) => {
        console.log(err)
    })
}).catch((err) => {
    console.log(err)
})

function doWork() {
    worker.postMessage(store.get(constants.METHOD))
}

function handleSignal(signal) {
    console.log(signal)
    process.exit(0)
}

process.on('SIGINT', handleSignal)
process.on('SIGTERM', handleSignal)
