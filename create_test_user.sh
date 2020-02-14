#!/bin/bash  
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ 
   "username": "e8ms2cu1fese7h8k6fcog@test.no", 
   "email": "e8ms2cu1fese7h8k6fcog@test.no", 
   "password": "e8ms2cu1fese7h8k6fcog"  
 }' 'http://localhost:3000/api/register'

curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ 
   "username": "5xj5g8gbt9gs9tp527znb8@test.no", 
   "email": "5xj5g8gbt9gs9tp527znb8@test.no", 
   "password": "5xj5g8gbt9gs9tp527znb8"  
 }' 'http://localhost:3000/api/register'

psql -h localhost -U $DBUSER -d $DBNAME -c "update public.user set activated = 't' where username = 'e8ms2cu1fese7h8k6fcog@test.no';"

psql -h localhost -U $DBUSER -d $DBNAME -c "update public.user set activated = 't' where username = '5xj5g8gbt9gs9tp527znb8@test.no';"

