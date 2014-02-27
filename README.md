Teletask-api
============

An open source java REST API for Teletask domotics.

It is the purpose to generate a REST API for software developers or domotics enthousiasts, who are interested in generating their own control environment for the TELETASK domotics systems, so you can create your own user interface and connected solutions and services.

If you own a Teletask MICROS, you have access to the free DLL32 LIBRARY (TDS15132).  However, if you're a java programmer like myself, you don't want to use a windows dll :-)
Started discussing the possibilities on the Teletask forum: http://www.teletask.be/forum/default.aspx?g=posts&t=195
And ended up programming a java interface based on IP Sockets, exposed by a basic JSON REST service.

Initially only setting and getting RELAYS is supported.
On the roadmap: support all micros features (moods, dimming, ...) and make the API more generic.
Current status: proof of concept.
