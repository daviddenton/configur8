resolvers += Resolver.url("bintray-sbt-plugin-releases", url("http://dl.bintray.com/content/sbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns)

resolvers += Classpaths.sbtPluginReleases

// removed temporarily whilst scoverage has no 2.12.0 version
//addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.0.4")
//
//addSbtPlugin("org.scoverage" % "sbt-coveralls" % "1.0.0.BETA1")

addSbtPlugin("me.lessis" % "bintray-sbt" % "0.1.2")
