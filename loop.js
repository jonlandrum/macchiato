const { Main } = require('electron');
const robot = require('robotjs')
const path = require('path')
const constants = require(path.join(__dirname, 'constants.js'))

robot.setKeyboardDelay(0)

onmessage = function (ev) {
    if (ev.data === constants.MOUSE_MOVE) {
        var pos = robot.getMousePos()
        robot.dragMouse(pos.x - 1, pos.y - 1);
    } else if (ev.data === constants.KEY_PRESS) {
        robot.keyTap('f15')
    }
    postMessage(ev.data)
};
