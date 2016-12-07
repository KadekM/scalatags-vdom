

[![Build Status](https://travis-ci.org/KadekM/scalatags-vdom.svg?branch=master)](https://travis-ci.org/KadekM/scalatags-vdom)
[![Maven Central](https://img.shields.io/maven-central/v/com.marekkadek/scalatags-vdom_sjs0.6_2.11.svg)](https://maven-badges.herokuapp.com/maven-central/com.marekkadek/scalatags-vdom_sjs0.6_2.11)

# scalatags-vdom

This project is a [Scalatags](https://github.com/lihaoyi/scalatags/) backend for [virtual-dom](https://github.com/Matt-Esch/virtual-dom).

It includes a barebones Scala.js mapping to the virtual-dom project in ```scalatags.vdom.raw.VirtualDom```. 

## Install

```scala
libraryDependencies += "com.marekkadek" %%% "scalatags-vdom" % "0.4.0-SNAPSHOT"
```


## Usage

It's usage is identical to the other Scalatags backends:

```scala
// import scalatags.Text.all._
// OR
// import scalatags.JsDom.all._
// OR
// import scalatags.VDom.all._
html(
  head(
    script(src:="..."),
    script(
      "alert('Hello World')"
    )
  ),
  body(
    div(
      h1(id:="title", "This is a title"),
      p("This is a big paragraph of text")
    )
  )
)
```
