#!/bin/bash  
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ 
   "username": "e8ms2cu1fese7h8k6fcog@test.no", 
   "email": "e8ms2cu1fese7h8k6fcog@test.no", 
   "password": "e8ms2cu1fese7h8k6fcog"  
 }' 'http://localhost:3000/api/register'

psql -h localhost -U $DBUSER -d $DBNAME -c "update public.user set activated = 't' where username = 'e8ms2cu1fese7h8k6fcog@test.no';"
