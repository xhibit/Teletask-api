# Teletask-api

An open source java JSON REST API for Teletask domotics.

It is the purpose to generate a REST API for software developers or domotics enthusiasts, who are interested in generating their own control environment for the TELETASK domotics systems, so you can create your own user interface and connected solutions and services.

If you own a Teletask MICROS (note: not the latest MICROS+), you have access to the free DLL32 LIBRARY (TDS15132).  However, if you're a java programmer like myself, you don't want to use a windows dll :-)
Bought me a RS232 > LAN converter (TDS10118) so I could access my MICROS server through regular IP.

Started discussing the possibilities on the Teletask forum: http://www.teletask.be/forum/default.aspx?g=posts&t=195
...and ended up programming a java interface based on IP Sockets, exposed by a basic JSON REST service.

Initially only setting and getting RELAYS, MTRUPDOWN, GENMOOD, LOCMOOD is supported.

### How this API works:

- Your Teletask config is read from the tds-config.json file.  This file holds all info on your personal setup.
- Once configured correctly, build the war file using maven.
- Deploy to a java app server on a device on your local network, so it has access to your MICROS server.  I used a low power [Raspberry PI](http://www.raspberrypi.org)

### Roadmap:

Current status: fully working REST API.

Future release:

- Support all micros features (dimming, check flags, conditions...)
- Add iBeacon support: this way I can use presence detection and switch relays depending on being close to a beacon.
- Add support for an [iTach Flex](http://www.globalcache.com/products/itachflex/) device so I can control additional devices through IR (airco units, projector, tv, ....
- A HTML 5 GUI for access on a mobile device (possibly using Twitter Bootstrap, Sencha Touch, ...).

