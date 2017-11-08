TODO





SLOW!

	at java.net.Inet6AddressImpl.getHostByAddr(Native Method)
	at java.net.InetAddress$2.getHostByAddr(InetAddress.java:932)
	at java.net.InetAddress.getHostFromNameService(InetAddress.java:617)
	at java.net.InetAddress.getCanonicalHostName(InetAddress.java:588)
	at org.apache.solr.handler.admin.SystemInfoHandler.initHostname(SystemInfoHandler.java:113)
	at org.apache.solr.handler.admin.SystemInfoHandler.<init>(SystemInfoHandler.java:99)
	at org.apache.solr.handler.admin.InfoHandler.<init>(InfoHandler.java:52)
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62)
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
	at java.lang.reflect.Constructor.newInstance(Constructor.java:423)
	at org.apache.solr.core.SolrResourceLoader.newInstance(SolrResourceLoader.java:637)
	at org.apache.solr.core.CoreContainer.createHandler(CoreContainer.java:1321)
	at org.apache.solr.core.CoreContainer.load(CoreContainer.java:488)
	at org.hrodberaht.injection.plugin.junit.solr.SolrTestRunner.createSolrContainer(SolrTestRunner.java:137)
	at org.hrodberaht.injection.plugin.junit.solr.SolrTestRunner.lambda$perpareSolrHomeAndStart$0(SolrTestRunner.java:116)
	at org.hrodberaht.injection.plugin.junit.solr.SolrTestRunner$$Lambda$47/1138697171.apply(Unknown Source)
	at java.util.concurrent.ConcurrentHashMap.computeIfAbsent(ConcurrentHashMap.java:1660)