resolvers += Resolver.jcenterRepo

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.9")
addSbtPlugin("net.aichler" % "sbt-jupiter-interface" % "0.9.1")
addSbtPlugin("com.github.xuwei-k" % "sbt-jshell" % "0.1.2")
addSbtPlugin("com.hanhuy.sbt" % "kotlin-plugin" % "2.0.0")

// run sbt dependencyUpdates
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.4.0")

