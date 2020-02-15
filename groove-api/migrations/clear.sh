psql -U lyseggen -d groove_api -a -f clear_tables.sql
psql -U lyseggen -d groove_api -a -f create_tables.sql
sh ../../create_test_user.sh
