# Macchiato ☕

A C++/Qt program that keeps your computer awake. It works in one of two ways:

* By occasionally entering a (generally) innocuous key stroke (F15), or
* By occasionally moving the mouse pointer slightly (one pixel over and then back to start)

As it is impossible to predict every user's requirements, both options are included in the settings; the default is the
key press option. And because this program utilizes the Qt framework, almost all desktop platforms are supported.

## License

![GPLv3](resources/gplv3.png)

This application is free software, and is licensed under the GNU General Public License version 3.0. The full text of
this license can be found in the file [LICENSE.md](LICENSE.md).

---

**Other Licenses Used**

* The cross-platform display library used is [Qt](https://github.com/qt/qt5), and is licensed under the same GPL v. 3
  license as this program. The source code for this library is included as a git submodule in the `lib` directory.
* The autostart functionality is provided by the [QAutostart](https://github.com/b00f/qautostart) library, and is
  licensed under the [MIT license](https://mit-license.org/). The source code for this library is included as a git
  submodule in the `lib` directory.
* The icon used for this program is the "hot beverage" emoji from
  the [Google Noto emoji set](https://github.com/googlefonts/noto-emoji), and is licensed under
  the [Apache 2.0 license](https://www.apache.org/licenses/LICENSE-2.0). The source code for this icon is included as a
  git submodule in the `lib` directory.
