# Teletask-api

An open source java JSON REST API for Teletask domotics.

It is the purpose to generate a REST API for software developers or domotics enthusiasts, who are interested in generating their own control environment for the TELETASK domotics systems, so you can create your own user interface and connected solutions and services.

If you own a Teletask MICROS (note: not the latest MICROS+), you have access to the free DLL32 LIBRARY (TDS15132).  However, if you're a java programmer like myself, you don't want to use a windows dll :-)
Bought me a RS232 > LAN converter (TDS10118) so I could access my MICROS server through regular IP.

Started discussing the possibilities on the Teletask forum: http://www.teletask.be/forum/default.aspx?g=posts&t=195
...and ended up programming a java interface based on IP Sockets, exposed by a basic JSON REST service.

Initially only setting and getting RELAYS, MTRUPDOWN, GENMOOD, LOCMOOD, COND, FLAG is supported.

**UPDATE**: Added a basic HTML 5 GUI for access on a mobile device.  Fastest implementation proved to be jQuery Mobile.   Will be improved in the future, but for now I have a dynamical UI which suits it's purpose...

### How this API works:

- Your Teletask config is read from the tds-config.json file.  This file holds all info on your personal setup.
- Once configured correctly, build the war file using maven.
- Deploy to a java app server on a device on your local network, so it has access to your MICROS server.  I used a low power [Raspberry PI](http://www.raspberrypi.org)

#### Libraries / Technologies

- Maven 3
- JBoss RESTEasy
- Jackson JSON processor
- jQuery Mobile
- ...

### REST API


Base URL API
http://<host>:<port>/teletask/api

**Relay**

- URI: GET <base_url>/relay/{number}
  - Description: Gets the relay state.  Returns 0 for off, 1 for on.
- URI: PUT <base_url>/relay/{number}/state/{0/1}
  - Description: Switches the relay on/off.  Use 0 for off, 1 for on.


**Motor**

- URI: GET <base_url>/motor/{number}
  - Description: Gets the motor state.  0 for down, 1 for up.
- URI: PUT <base_url>/motor/{number}/state/{0/1}
  - Description: Switches the motor up or down.  Use 1 for up, 0 for down.

**Local Mood**

- URI: GET <base_url>/mood/local/{number}
  - Description: Gets the mood state.  Returns 0 for off, 1 for on.
- URI: PUT <base_url>/mood/local/{number}/state/{0/1}
  - Description: Switches the mood on/off.  Use 0 for off, 1 for on.

**General Mood**

- URI: GET <base_url>/mood/general/{number}
  - Description: Gets the mood state.  Returns 0 for off, 1 for on.
- URI: PUT <base_url>/mood/general/{number}/state/{0/1}
  - Description: Switches the mood on/off.  Use 0 for off, 1 for on.

**Condition**

- URI: GET <base_url>/condition/{number}
  - Description: Gets the condition state.  Returns 0 for false, 1 for true.

**Flag**

- URI: GET <base_url>/flag/{number}
  - Description: Gets the flag state.  Returns 0 for false, 1 for true.


### Roadmap:

Current status: fully working REST API & UI.

Future release:

- Support all micros features (dimming, ...)
- Add iBeacon support: this way I can use presence detection and switch relays depending on being close to a beacon.
- Add support for an [iTach Flex](http://www.globalcache.com/products/itachflex/) device so I can control additional devices through IR (airco units, projector, tv, ....
- A more advanced HTML 5 GUI for access on a mobile device (possibly using Twitter Bootstrap, Sencha Touch, ...).


