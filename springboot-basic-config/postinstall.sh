#!/bin/bash

echo "post install Admin-Server and API-Server"

if hash systemctl 2>/dev/null; then
   echo used systemctl
   sudo chmod 755 /var/lib/modules/Admin-Server/config/Admin-Server.service
   sudo ln -s /var/lib/modules/Admin-Server/config/Admin-Server.service /etc/systemd/system/Admin-Server.service
   sudo chmod 755 /var/lib/modules/API-Server/config/API-Server.service
   sudo ln -s /var/lib/modules/API-Server/config/API-Server.service /etc/systemd/system/API-Server.service
else
   echo used service
   sudo ln -s /var/lib/modules/Admin-Server/config/Admin-Server.service /etc/init.d/Admin-Server.service
   sudo ln -s /var/lib/modules/API-Server/config/API-Server.service /etc/init.d/API-Server.service
fi
