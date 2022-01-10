@echo off

REM --------------------------------------------------
REM Monster Trading Cards Game
REM --------------------------------------------------
title Monster Trading Cards Game
echo CURL Testing for Monster Trading Cards Game
echo.

REM --------------------------------------------------
echo 1) Create Users (Registration)
REM Create User
curl -X POST http://localhost:10001/users --header "Content-Type: application/json" -d "{\"Username\":\"kienboec\", \"Password\":\"daniel\"}"
echo.
curl -X POST http://localhost:10001/users --header "Content-Type: application/json" -d "{\"Username\":\"altenhof\", \"Password\":\"markus\"}"
echo.

echo should fail:
curl -X POST http://localhost:10001/users --header "Content-Type: application/json" -d "{\"Username\":\"kienboec\", \"Password\":\"daniel\"}"
echo.
curl -X POST http://localhost:10001/users --header "Content-Type: application/json" -d "{\"Username\":\"kienboec\", \"Password\":\"different\"}"
echo. 
echo.

REM --------------------------------------------------
echo 2) Login Users
curl -X POST http://localhost:10001/sessions --header "Content-Type: application/json" -d "{\"Username\":\"kienboec\", \"Password\":\"daniel\"}"
echo.
curl -X POST http://localhost:10001/sessions --header "Content-Type: application/json" -d "{\"Username\":\"altenhof\", \"Password\":\"markus\"}"
echo.
curl -X POST http://localhost:10001/sessions --header "Content-Type: application/json" -d "{\"Username\":\"admin\",    \"Password\":\"istrator\"}"
echo.

echo should fail:
curl -X POST http://localhost:10001/sessions --header "Content-Type: application/json" -d "{\"Username\":\"kienboec\", \"Password\":\"different\"}"
echo.
echo.

REM --------------------------------------------------
echo 3) create packages (done by "admin")
curl -X POST http://localhost:10001/packages --header "Content-Type: application/json" --header "Authorization: adminToken" -d "[\"package1\", {\"Id\":\"845f0dc7-37d0-426e-994e-43fc3ac83c08\", \"Name\":\"WaterGoblin\", \"Damage\": 10, \"Element\":\"Water\", \"Type\":\"Creature\"}, {\"Id\":\"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\", \"Name\":\"Dragon\", \"Damage\": 50, \"Element\":\"Fire\", \"Type\":\"Creature\"}, {\"Id\":\"e85e3976-7c86-4d06-9a80-641c2019a79f\", \"Name\":\"WaterSpell\", \"Damage\": 20, \"Element\":\"Water\", \"Type\":\"Spell\"}, {\"Id\":\"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\", \"Name\":\"Ork\", \"Damage\": 45, \"Element\":\"Plant\", \"Type\":\"Creature\"}, {\"Id\":\"dfdd758f-649c-40f9-ba3a-8657f4b3439f\", \"Name\":\"FireSpell\",    \"Damage\": 25, \"Element\":\"Fire\", \"Type\":\"Spell\"}]"
echo.																																																																																		 				    
curl -X POST http://localhost:10001/packages --header "Content-Type: application/json" --header "Authorization: adminToken" -d "[\"package2\", {\"Id\":\"644808c2-f87a-4600-b313-122b02322fd5\", \"Name\":\"WaterGoblin\", \"Damage\":  9, \"Element\":\"Water\", \"Type\":\"Creature\"}, {\"Id\":\"4a2757d6-b1c3-47ac-b9a3-91deab093531\", \"Name\":\"Dragon\", \"Damage\": 55, \"Element\":\"Fire\", \"Type\":\"Creature\"}, {\"Id\":\"91a6471b-1426-43f6-ad65-6fc473e16f9f\", \"Name\":\"WaterSpell\", \"Damage\": 21, \"Element\":\"Water\", \"Type\":\"Spell\"}, {\"Id\":\"4ec8b269-0dfa-4f97-809a-2c63fe2a0025\", \"Name\":\"Ork\", \"Damage\": 55, \"Element\":\"Plant\", \"Type\":\"Creature\"}, {\"Id\":\"f8043c23-1534-4487-b66b-238e0c3c39b5\", \"Name\":\"WaterSpell\",   \"Damage\": 23, \"Element\":\"Fire\", \"Type\":\"Spell\"}]"
echo.																																																																																		 				    
curl -X POST http://localhost:10001/packages --header "Content-Type: application/json" --header "Authorization: adminToken" -d "[\"package3\", {\"Id\":\"b017ee50-1c14-44e2-bfd6-2c0c5653a37c\", \"Name\":\"WaterGoblin\", \"Damage\": 11, \"Element\":\"Water\", \"Type\":\"Creature\"}, {\"Id\":\"d04b736a-e874-4137-b191-638e0ff3b4e7\", \"Name\":\"Dragon\", \"Damage\": 70, \"Element\":\"Fire\", \"Type\":\"Creature\"}, {\"Id\":\"88221cfe-1f84-41b9-8152-8e36c6a354de\", \"Name\":\"WaterSpell\", \"Damage\": 22, \"Element\":\"Water\", \"Type\":\"Spell\"}, {\"Id\":\"1d3f175b-c067-4359-989d-96562bfa382c\", \"Name\":\"Ork\", \"Damage\": 40, \"Element\":\"Plant\", \"Type\":\"Creature\"}, {\"Id\":\"171f6076-4eb5-4a7d-b3f2-2d650cc3d237\", \"Name\":\"RegularSpell\", \"Damage\": 28, \"Element\":\"Fire\", \"Type\":\"Spell\"}]"
echo.																																																																																		 				    
curl -X POST http://localhost:10001/packages --header "Content-Type: application/json" --header "Authorization: adminToken" -d "[\"package4\", {\"Id\":\"ed1dc1bc-f0aa-4a0c-8d43-1402189b33c8\", \"Name\":\"WaterGoblin\", \"Damage\": 10, \"Element\":\"Water\", \"Type\":\"Creature\"}, {\"Id\":\"65ff5f23-1e70-4b79-b3bd-f6eb679dd3b5\", \"Name\":\"Dragon\", \"Damage\": 50, \"Element\":\"Fire\", \"Type\":\"Creature\"}, {\"Id\":\"55ef46c4-016c-4168-bc43-6b9b1e86414f\", \"Name\":\"WaterSpell\", \"Damage\": 20, \"Element\":\"Water\", \"Type\":\"Spell\"}, {\"Id\":\"f3fad0f2-a1af-45df-b80d-2e48825773d9\", \"Name\":\"Ork\", \"Damage\": 45, \"Element\":\"Plant\", \"Type\":\"Creature\"}, {\"Id\":\"8c20639d-6400-4534-bd0f-ae563f11f57a\", \"Name\":\"WaterSpell\",   \"Damage\": 25, \"Element\":\"Fire\", \"Type\":\"Spell\"}]"
echo.																																																																																		 				    
curl -X POST http://localhost:10001/packages --header "Content-Type: application/json" --header "Authorization: adminToken" -d "[\"package5\", {\"Id\":\"d7d0cb94-2cbf-4f97-8ccf-9933dc5354b8\", \"Name\":\"WaterGoblin\", \"Damage\":  9, \"Element\":\"Water\", \"Type\":\"Creature\"}, {\"Id\":\"44c82fbc-ef6d-44ab-8c7a-9fb19a0e7c6e\", \"Name\":\"Dragon\", \"Damage\": 55, \"Element\":\"Fire\", \"Type\":\"Creature\"}, {\"Id\":\"2c98cd06-518b-464c-b911-8d787216cddd\", \"Name\":\"WaterSpell\", \"Damage\": 21, \"Element\":\"Water\", \"Type\":\"Spell\"}, {\"Id\":\"951e886a-0fbf-425d-8df5-af2ee4830d85\", \"Name\":\"Ork\", \"Damage\": 55, \"Element\":\"Plant\", \"Type\":\"Creature\"}, {\"Id\":\"dcd93250-25a7-4dca-85da-cad2789f7198\", \"Name\":\"FireSpell\",    \"Damage\": 23, \"Element\":\"Fire\", \"Type\":\"Spell\"}]"
echo.																																																																																		 				    
curl -X POST http://localhost:10001/packages --header "Content-Type: application/json" --header "Authorization: adminToken" -d "[\"package6\", {\"Id\":\"b2237eca-0271-43bd-87f6-b22f70d42ca4\", \"Name\":\"WaterGoblin\", \"Damage\": 11, \"Element\":\"Water\", \"Type\":\"Creature\"}, {\"Id\":\"9e8238a4-8a7a-487f-9f7d-a8c97899eb48\", \"Name\":\"Dragon\", \"Damage\": 70, \"Element\":\"Fire\", \"Type\":\"Creature\"}, {\"Id\":\"d60e23cf-2238-4d49-844f-c7589ee5342e\", \"Name\":\"WaterSpell\", \"Damage\": 22, \"Element\":\"Water\", \"Type\":\"Spell\"}, {\"Id\":\"fc305a7a-36f7-4d30-ad27-462ca0445649\", \"Name\":\"Ork\", \"Damage\": 40, \"Element\":\"Plant\", \"Type\":\"Creature\"}, {\"Id\":\"84d276ee-21ec-4171-a509-c1b88162831c\", \"Name\":\"RegularSpell\", \"Damage\": 28, \"Element\":\"Fire\", \"Type\":\"Spell\"}]"
echo.
echo.

REM --------------------------------------------------
echo 4) acquire packages kienboec
curl -X POST http://localhost:10001/transactions/packages --header "Content-Type: application/json" --header "Authorization: kienboec-mtcgToken" -d "\"package1\""
echo.
curl -X POST http://localhost:10001/transactions/packages --header "Content-Type: application/json" --header "Authorization: kienboec-mtcgToken" -d "\"package2\""
echo.
curl -X POST http://localhost:10001/transactions/packages --header "Content-Type: application/json" --header "Authorization: kienboec-mtcgToken" -d "\"package3\""
echo.
curl -X POST http://localhost:10001/transactions/packages --header "Content-Type: application/json" --header "Authorization: kienboec-mtcgToken" -d "\"package4\""
echo.
echo should fail (no money):
curl -X POST http://localhost:10001/transactions/packages --header "Content-Type: application/json" --header "Authorization: kienboec-mtcgToken" -d "\"package5\""
echo.
echo.

REM --------------------------------------------------
echo 5) acquire packages altenhof
curl -X POST http://localhost:10001/transactions/packages --header "Content-Type: application/json" --header "Authorization: altenhof-mtcgToken" -d "\"package5\""
echo.
curl -X POST http://localhost:10001/transactions/packages --header "Content-Type: application/json" --header "Authorization: altenhof-mtcgToken" -d "\"package6\""
echo.
echo should fail (no package):
curl -X POST http://localhost:10001/transactions/packages --header "Content-Type: application/json" --header "Authorization: altenhof-mtcgToken" -d "\"package20\""
echo.
echo.

REM --------------------------------------------------
echo 6) add new packages
curl -X POST http://localhost:10001/packages --header "Content-Type: application/json" --header "Authorization: adminToken" -d "[\"package7\", {\"Id\":\"67f9048f-99b8-4ae4-b866-d8008d00c53d\", \"Name\":\"WaterGoblin\", \"Damage\": 10, \"Element\":\"Water\", \"Type\":\"Creature\"}, {\"Id\":\"aa9999a0-734c-49c6-8f4a-651864b14e62\", \"Name\":\"RegularSpell\", \"Damage\": 50, \"Element\":\"Normal\", \"Type\":\"Spell\"}, {\"Id\":\"d6e9c720-9b5a-40c7-a6b2-bc34752e3463\", \"Name\":\"Knight\", \"Damage\": 20, \"Element\":\"Normal\", \"Type\":\"Creature\"}, {\"Id\":\"02a9c76e-b17d-427f-9240-2dd49b0d3bfd\", \"Name\":\"RegularSpell\", \"Damage\": 45, \"Element\":\"Normal\", \"Type\":\"Spell\"}, {\"Id\":\"2508bf5c-20d7-43b4-8c77-bc677decadef\", \"Name\":\"FireElf\", \"Damage\": 25, \"Element\":\"Fire\", \"Type\":\"Creature\"}]"
echo.
curl -X POST http://localhost:10001/packages --header "Content-Type: application/json" --header "Authorization: adminToken" -d "[\"package8\", {\"Id\":\"70962948-2bf7-44a9-9ded-8c68eeac7793\", \"Name\":\"WaterGoblin\", \"Damage\":  9, \"Element\":\"Water\", \"Type\":\"Creature\"}, {\"Id\":\"74635fae-8ad3-4295-9139-320ab89c2844\", \"Name\":\"FireSpell\", \"Damage\": 55, \"Element\":\"Fire\", \"Type\":\"Spell\"}, {\"Id\":\"ce6bcaee-47e1-4011-a49e-5a4d7d4245f3\", \"Name\":\"Knight\", \"Damage\": 21, \"Element\":\"Normal\", \"Type\":\"Creature\"}, {\"Id\":\"a6fde738-c65a-4b10-b400-6fef0fdb28ba\", \"Name\":\"FireSpell\", \"Damage\": 55, \"Element\":\"Fire\", \"Type\":\"Spell\"}, {\"Id\":\"a1618f1e-4f4c-4e09-9647-87e16f1edd2d\", \"Name\":\"FireElf\", \"Damage\": 23, \"Element\":\"Fire\", \"Type\":\"Creature\"}]"
echo.
curl -X POST http://localhost:10001/packages --header "Content-Type: application/json" --header "Authorization: adminToken" -d "[\"package9\", {\"Id\":\"2272ba48-6662-404d-a9a1-41a9bed316d9\", \"Name\":\"WaterGoblin\", \"Damage\": 11, \"Element\":\"Water\", \"Type\":\"Creature\"}, {\"Id\":\"3871d45b-b630-4a0d-8bc6-a5fc56b6a043\", \"Name\":\"Dragon\", \"Damage\": 70, \"Element\":\"Fire\", \"Type\":\"Creature\"}, {\"Id\":\"166c1fd5-4dcb-41a8-91cb-f45dcd57cef3\", \"Name\":\"Knight\", \"Damage\": 22, \"Element\":\"Normal\", \"Type\":\"Creature\"}, {\"Id\":\"237dbaef-49e3-4c23-b64b-abf5c087b276\", \"Name\":\"WaterSpell\", \"Damage\": 40, \"Element\":\"Water\", \"Type\":\"Spell\"}, {\"Id\":\"27051a20-8580-43ff-a473-e986b52f297a\", \"Name\":\"FireElf\", \"Damage\": 28, \"Element\":\"Fire\", \"Type\":\"Creature\"}]"
echo.
echo.

REM --------------------------------------------------
echo 7) acquire newly created packages altenhof
curl -X POST http://localhost:10001/transactions/packages --header "Content-Type: application/json" --header "Authorization: altenhof-mtcgToken" -d "\"package8\""
echo.
curl -X POST http://localhost:10001/transactions/packages --header "Content-Type: application/json" --header "Authorization: altenhof-mtcgToken" -d "\"package9\""
echo.
echo should fail (no money):
curl -X POST http://localhost:10001/transactions/packages --header "Content-Type: application/json" --header "Authorization: altenhof-mtcgToken" -d "\"package2\""
echo.
echo.

REM --------------------------------------------------
echo 8) show all acquired cards kienboec
curl -X GET http://localhost:10001/cards --header "Authorization: kienboec-mtcgToken"
echo should fail (no token)
curl -X GET http://localhost:10001/cards 
echo.
echo.

REM --------------------------------------------------
echo 9) show all acquired cards altenhof
curl -X GET http://localhost:10001/cards --header "Authorization: altenhof-mtcgToken"
echo.
echo.

REM --------------------------------------------------
echo 10) show unconfigured deck
curl -X GET http://localhost:10001/deck --header "Authorization: kienboec-mtcgToken"
echo.
curl -X GET http://localhost:10001/deck --header "Authorization: altenhof-mtcgToken"
echo.
echo.

REM --------------------------------------------------
echo 11) configure deck
curl -X PUT http://localhost:10001/deck --header "Content-Type: application/json" --header "Authorization: altenhof-mtcgToken" -d "[\"d7d0cb94-2cbf-4f97-8ccf-9933dc5354b8\", \"44c82fbc-ef6d-44ab-8c7a-9fb19a0e7c6e\", \"2c98cd06-518b-464c-b911-8d787216cddd\", \"951e886a-0fbf-425d-8df5-af2ee4830d85\"]"
echo.
curl -X GET http://localhost:10001/deck --header "Authorization: altenhof-mtcgToken"
echo.
curl -X PUT http://localhost:10001/deck --header "Content-Type: application/json" --header "Authorization: kienboec-mtcgToken" -d "[\"845f0dc7-37d0-426e-994e-43fc3ac83c08\", \"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\", \"e85e3976-7c86-4d06-9a80-641c2019a79f\", \"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\"]"
echo.
curl -X GET http://localhost:10001/deck --header "Authorization: kienboec-mtcgToken"
echo.
echo.
echo should fail and show original from before:
curl -X PUT http://localhost:10001/deck --header "Content-Type: application/json" --header "Authorization: altenhof-mtcgToken" -d "[\"d7d0cb94-2cbf-4f97-8ccf-9933dc5354b8\", \"44c82fbc-ef6d-44ab-8c7a-9fb19a0e7c6e\", \"2c98cd06-518b-464c-b911-8d787216cddd\", \"951e886a-0fbf-425d-8df5-af2ee4830d85\"]"
echo.
curl -X GET http://localhost:10001/deck --header "Authorization: altenhof-mtcgToken"
echo.
echo.
echo should fail ... only 3 cards set
curl -X PUT http://localhost:10001/deck --header "Content-Type: application/json" --header "Authorization: altenhof-mtcgToken" -d "[\"aa9999a0-734c-49c6-8f4a-651864b14e62\", \"d6e9c720-9b5a-40c7-a6b2-bc34752e3463\", \"d60e23cf-2238-4d49-844f-c7589ee5342e\"]"
echo.


REM --------------------------------------------------
echo 12) show configured deck 
curl -X GET http://localhost:10001/deck --header "Authorization: kienboec-mtcgToken"
echo.
curl -X GET http://localhost:10001/deck --header "Authorization: altenhof-mtcgToken"
echo.
echo.

REM --------------------------------------------------
echo 13) show configured deck different representation
echo kienboec
curl -X GET http://localhost:10001/deck?format=plain --header "Authorization: kienboec-mtcgToken"
echo.
echo.
echo altenhof
curl -X GET http://localhost:10001/deck?format=plain --header "Authorization: altenhof-mtcgToken"
echo.
echo.

REM --------------------------------------------------
echo 14) edit Profile
echo.
curl -X GET http://localhost:10001/users/kienboec --header "Authorization: kienboec-mtcgToken"
echo.
curl -X GET http://localhost:10001/users/altenhof --header "Authorization: altenhof-mtcgToken"
echo.
curl -X PUT http://localhost:10001/users/kienboec --header "Content-Type: application/json" --header "Authorization: kienboec-mtcgToken" -d "{\"Name\": \"Kienboeck\",  \"Bio\": \"me playin...\", \"Image\": \":-)\"}"
echo.
curl -X PUT http://localhost:10001/users/altenhof --header "Content-Type: application/json" --header "Authorization: altenhof-mtcgToken" -d "{\"Name\": \"Altenhofer\", \"Bio\": \"me codin...\",  \"Image\": \":-D\"}"
echo.
curl -X GET http://localhost:10001/users/kienboec --header "Authorization: kienboec-mtcgToken"
echo.
curl -X GET http://localhost:10001/users/altenhof --header "Authorization: altenhof-mtcgToken"
echo.
echo.
echo should fail:
curl -X GET http://localhost:10001/users/altenhof --header "Authorization: kienboec-mtcgToken"
echo.
curl -X GET http://localhost:10001/users/kienboec --header "Authorization: altenhof-mtcgToken"
echo.
curl -X PUT http://localhost:10001/users/kienboec --header "Content-Type: application/json" --header "Authorization: altenhof-mtcgToken" -d "{\"Name\": \"Hoax\",  \"Bio\": \"me playin...\", \"Image\": \":-)\"}"
echo.
curl -X PUT http://localhost:10001/users/altenhof --header "Content-Type: application/json" --header "Authorization: kienboec-mtcgToken" -d "{\"Name\": \"Hoax\", \"Bio\": \"me codin...\",  \"Image\": \":-D\"}"
echo.
curl -X GET http://localhost:10001/users/someGuy  --header "Authorization: kienboec-mtcgToken"
echo.
echo.

REM --------------------------------------------------
echo 15) scoreboard
curl -X GET http://localhost:10001/score --header "Authorization: kienboec-mtcgToken"
echo.
echo.

REM --------------------------------------------------
echo 16) battle
start /b "kienboec battle" curl -X POST http://localhost:10001/battles --header "Authorization: kienboec-mtcgToken"
start /b "altenhof battle" curl -X POST http://localhost:10001/battles --header "Authorization: altenhof-mtcgToken"
ping localhost -n 10 >NUL 2>NUL

REM --------------------------------------------------
echo 17) Profile
echo kienboec
curl -X GET http://localhost:10001/users/kienboec --header "Authorization: kienboec-mtcgToken"
echo.
curl -X GET http://localhost:10001/users/altenhof --header "Authorization: altenhof-mtcgToken"
echo.echo.
echo.

REM --------------------------------------------------
echo 18) scoreboard
curl -X GET http://localhost:10001/score --header "Authorization: kienboec-mtcgToken"
echo.
echo.

REM --------------------------------------------------
echo 19) trade
echo check trading deals
curl -X GET http://localhost:10001/tradings --header "Authorization: kienboec-mtcgToken"
echo.
echo create trading deal
curl -X POST http://localhost:10001/tradings --header "Content-Type: application/json" --header "Authorization: kienboec-mtcgToken" -d "{\"Id\": \"6cd85277-4590-49d4-b0cf-ba0a921faad0\", \"CardToTrade\": \"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\", \"Type\": \"monster\", \"MinimumDamage\": 15}"
echo.
echo check trading deals
curl -X GET http://localhost:10001/tradings --header "Authorization: kienboec-mtcgToken"
echo.
curl -X GET http://localhost:10001/tradings --header "Authorization: altenhof-mtcgToken"
echo.
echo delete trading deals
curl -X DELETE http://localhost:10001/tradings/6cd85277-4590-49d4-b0cf-ba0a921faad0 --header "Authorization: kienboec-mtcgToken"
echo.
echo.

REM --------------------------------------------------
echo 20) check trading deals
curl -X GET http://localhost:10001/tradings  --header "Authorization: kienboec-mtcgToken"
echo.
curl -X POST http://localhost:10001/tradings --header "Content-Type: application/json" --header "Authorization: kienboec-mtcgToken" -d "{\"Id\": \"6cd85277-4590-49d4-b0cf-ba0a921faad0\", \"CardToTrade\": \"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\", \"Type\": \"monster\", \"MinimumDamage\": 15}"
echo check trading deals
curl -X GET http://localhost:10001/tradings  --header "Authorization: kienboec-mtcgToken"
echo.
curl -X GET http://localhost:10001/tradings  --header "Authorization: altenhof-mtcgToken"
echo.
echo try to trade with yourself (should fail)
curl -X POST http://localhost:10001/tradings/6cd85277-4590-49d4-b0cf-ba0a921faad0 --header "Content-Type: application/json" --header "Authorization: kienboec-mtcgToken" -d "\"4ec8b269-0dfa-4f97-809a-2c63fe2a0025\""
echo.
echo try to trade 
echo.
curl -X POST http://localhost:10001/tradings/6cd85277-4590-49d4-b0cf-ba0a921faad0 --header "Content-Type: application/json" --header "Authorization: altenhof-mtcgToken" -d "\"951e886a-0fbf-425d-8df5-af2ee4830d85\""
echo.
curl -X GET http://localhost:10001/tradings --header "Authorization: kienboec-mtcgToken"
echo.
curl -X GET http://localhost:10001/tradings --header "Authorization: altenhof-mtcgToken"
echo.

REM --------------------------------------------------
echo end...

REM this is approx a sleep 
ping localhost -n 100 >NUL 2>NUL
@echo on
