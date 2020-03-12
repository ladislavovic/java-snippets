* shell represents a window
* display manage event loops, fonts, colors, .... Controlls communication between UI thread and other threads
* JFace - build on top of SWT, higher level of abstraction. But
  it does not hide SWT API. If you want to work with JFace, you
  need to know SWT
* Widgets with C in the name at the beginning are widgets, which
  customize default widget with some additional functionality. C means "Custom". For example CCombo.
* Widgets from the package org.eclipse.swt.custom are implemented in pure Java while widgets from org.eclipse.swt.widgets use native code.
* Widgets extend either the Widget or the Control
* in SWT terminology Widget and Control means the same and are interchangeable

  
TODO continue with 3. SWT Widgets
