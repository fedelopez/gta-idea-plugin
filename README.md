[![Build Status](https://buildhive.cloudbees.com/job/fedelopez/job/gta-idea-plugin/badge/icon)](https://buildhive.cloudbees.com/job/fedelopez/job/gta-idea-plugin/)

Introduction
============
* * *

IntelliJ plugin to facilitate the edition of the Grand Test Auto settings file. This file serves to configure, among other things, the suite of tests to be launched.
GTA is a powerful open source Java test harness developed by Tim Lavers.

Visit the [Grand Test Auto](http://grandtestauto.org/ "GTA") website for more info about this test harness.

You can use this project as Maven archetype to build your own IntelliJ Plugin.

Basic usage
===========
* * *

Initial Setup
-------------

* Open the IntelliJ Settings dialog (Ctrl + Alt + S)
* Select the __Grand Test Auto__ menu item from the __IDE Settings__ panel
* The plugin editor GUI will appear on the right-hand side
* On the plugin editor, type the file path where the GTA configuration settings file resides

Now you can start using the plugin.

Note: you can use the variable __$MODULE_DIR$__ if you want to take advantage of the IntelliJ path variables.

Invoking the plugin
-------------------

The plugin can be invoked when one of the following items has been selected:

- A class (e.g. Foo.class)
- A test class (e.g. FooTest.class)
- A class method (e.g. a method signature on Foo.class or FooTest.class)
- A package (e.g. a package statement on Foo.class or FooTest.class)

Click or focus the caret on any one of the items above and invoke the plugin by:

* right-clicking the mouse and selecting the menu item __Update GTA Settings__
* using the shortcut __Ctrl + Shift + G__

The plugin will be invoked and configure the GTA Settings file according to the selected item.

Examples
--------

* Invoking the plugin on the method signature of a class will run only the test for that method
* Invoking the plugin anywhere on a class will run all the tests for that class
* Invoking the plugin on a class package statement will run all the test for that package