




INSERT INTO roles (id, name, display_name) VALUES
(1, 'ROLE_ADMIN','Admin'),
(2, 'ROLE_SUPER','Super Admin');

INSERT INTO users (id, email, password, display_name) VALUES
(1, 'admin@gmail.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS','Admin'),
-- (2, 'super@gmail.com', '$2a$10$UFEPYW7Rx1qZqdHajzOnB.VBR3rvm7OI7uSix4RadfQiNhkZOi2fi','Super'),
(2, 'super@gmail.com', '$2a$10$oj3Sjj6HZdp5P43EjDiPx.R0fVpYvFoPpC8TTQpOno5erjcmFbrSC','Super');



insert into user_role(user_id, role_id) values
(1,1),
(2,2);


INSERT INTO ref_language(language_id,name) VALUES
(1,'English'),(2,'French'),(3,'Japanese'),(4,'Spanish');

INSERT INTO ref_movie_format(format_id,format) VALUES
(1,'DVD'),(2,'BluRay');

INSERT INTO ref_book_format(format_id,format) VALUES
(1,'Paperback'),(2,'Hardcover'),(3,'E-Book');




INSERT INTO product VALUES
(1,'2018-07-25 11:37:25','Hugh Jackman leads an all-star cast in this bold and original musical filled with infectious showstopping performances that will bring you to your feet time and time again. Inspired by the story of P.T. Barnum (Jackman) and celebrating the birth of show business, the film follows the visionary who rose from nothing to create a mesmerising spectacle. This inspirational film also stars Zac Efron, Michelle Williams, Rebecca Ferguson and Zendaya.','1.jpg',15.00,'MOVIE','The Greatest Showman','2018-07-25 11:39:27'),
(2,'2018-07-25 11:57:13','Can two people who have never met make a marriage work? Popular dating site Sociality thinks so, and is marrying London lad Adam to California girl Jessica to prove it.','2.jpg',10.00,'BOOK','Mad Love','2018-07-25 11:57:31'),
(3,'2018-07-25 11:58:24','When siblings Dan and Hayley Daley inherit their late grandmother’s derelict Victorian farmhouse, it seems like a dream come true. All they have to do is fix the place up and sell it for a tidy profit!','3.jpg',5.00,'BOOK','Bricking It','2018-07-25 11:58:33'),
(4,'2018-07-25 12:00:34','An escaped cannibal, a family curse ... and Reginald Worcester turning up on the doorstep. Could things get any worse for the Baskerville-Smythe family? As the bodies pile up, only a detective with a rare brain – and Reggie’s is so rare it’s positively endangered – can even hope to solve the case. ','4.jpg',15.00,'BOOK','The Unpleasantness at Baskerville Hall ','2018-07-25 12:00:46'),
(5,'2018-07-25 12:54:24','A Good Man in Africa is William Boyd''s classic, prize-winning debut novel - Winner of the Whitbread Award and the Somerset Maugham Prize','5.jpg',35.00,'BOOK','A Good Man in Africa','2018-07-25 12:54:35'),
(6,'2018-07-25 12:55:13','Eva Delectorskaya, a beautiful Russian émigrée living in Paris, is recruited for the British Secret Service by the mysterious Lucas Romer. Under his tutelage she becomes the perfect spy – trusting no one, even those she loves most. ','6.jpg',16.00,'BOOK','Restless','2018-07-25 12:55:24'),
(7,'2018-07-25 12:55:55','Every mother''s worst nightmare becomes a shocking reality in this terrifying and emotional roller-coaster set on the Cornish coast','7.jpg',3.00,'BOOK','The Cove','2018-07-25 12:56:16'),
(8,'2018-07-25 12:57:08','Read the definitive edition of Bilbo Baggins’ adventures in middle-earth in this classic bestseller behind this year’s biggest movie','8.jpg',45.00,'BOOK','The Hobbit','2018-07-25 12:57:27'),
(9,'2018-07-25 12:57:55','The intergalactic adventures of Arthur Dent begin in the first volume of the ''trilogy of five'', Douglas Adams'' comedy sci-fi classic The Hitchhiker''s Guide to the Galaxy.','9.jpg',2.00,'BOOK','The Hitchhiker''s Guide to the Galaxy','2018-07-25 12:58:13'),
(10,'2018-07-25 12:58:34','NOW A MAJOR MOTION PICTURE DIRECTED BY STEVEN SPIELBERG\r\n\r\n**A world at stake. A quest for the ultimate prize. Are you ready?**','10.jpg',27.00,'BOOK','Ready Player One','2018-07-25 12:58:46'),
(11,'2018-07-25 12:59:36','‘Vimes ran a practised eye over the assortment before him. It was the usual Ankh-Morpork mob in times of crisis; half of them were here to complain, a quarter of them were here to watch the other half, and the remainder were here to rob, importune or sell hotdogs to the rest.’','11.jpg',7.00,'BOOK','Guards! Guards!','2018-07-25 12:59:44'),
(12,'2018-07-25 13:00:26','It''s Midsummer Night - no time for dreaming. Because sometimes, when there''s more than one reality at play, too much dreaming can make the walls between them come tumbling down. Unfortunately there''s usually a damned good reason for there being walls between them in the first place - to keep things out. Things who want to make mischief and play havoc with the natural order.','12.jpg',12.00,'BOOK','Lords And Ladies: (Discworld Novel 14)','2018-07-25 13:00:37'),


(19,'2018-07-25 13:07:49','In Disney•Pixar’s vibrant tale of family, fun and adventure, an aspiring young musician named Miguel (voice of newcomer Anthony Gonzalez) embarks on an extraordinary journey to the magical land of his ancestors. There, the charming trickster Hector (voice of Gael Garcia Bernal) becomes an unexpected friend who helps Miguel uncover the mysteries behind his family''s stories and traditions.','19.jpg',15.00,'MOVIE','Coco ','2018-07-25 13:08:12'),
(20,'2018-07-25 13:08:58','After young Riley is uprooted from her Midwest life and moved to San Francisco, her emotions - joy, fear, anger, disgust and sadness - conflict on how best to navigate a new city, house and school.','20.jpg',8.00,'MOVIE','Inside Out','2018-07-25 13:09:26'),
(21,'2018-07-25 13:10:22','This a Disney adaptation of Robert Louis Stevenson''s classic tale ''Treasure Island'' only set way in the future. Young Jim Hawkins has been brought up solely by his mother after his father had deserted them, he grows up reading hearing mythical stories of Captain Flint''s treasure.','21.jpg',45.00,'MOVIE','Treasure Planet','2018-07-25 13:10:42'),
(22,'2018-07-25 13:11:20','Animated musical adventure from Disney. When an eviction notice goes up at the Little Piece of Heaven family-run dairy farm, notorious yodelling outlaw cattle rustler Alameda Slim (voice of Randy Quaid) sees his big chance to claim it for himself. However, he hasn''t counted on three resourceful dairy cows, old-timer Mrs Calloway (Judi Dench), tough-talking Maggie (Roseanne Barr) and gentle Grace (Jennifer Tilly), who enlist the help of the other farm animals to track down Slim and use the ransom on his head to save their beloved farm. But the bovine trio have another enemy to contend with; ruthless bounty hunter Rico (Charles Dennis) is also after the reward for capturing Slim. Bonnie Raitt and k.d. lang provide voice talent for the film''s songs.','22.jpg',3.00,'MOVIE','Home On The Range ','2018-07-25 13:11:30'),
(23,'2018-07-25 13:12:15','ALL THE MONEY IN THE WORLD follows the kidnapping of 16-year-old John Paul Getty III and the desperate attempt by his devoted mother Gail to convince his billionaire grandfather to pay the ransom. When Getty Sr. refuses, Gail attempts to sway him as her son’s captors become increasingly volatile and brutal. With her son’s life in the balance, Gail and Getty’s advisor become unlikely allies in the race against time that ultimately reveals the true and lasting value of love over money.','23.jpg',9.00,'MOVIE','All The Money In The World','2018-07-25 13:12:28'),
(24,'2018-07-25 13:13:03','Lara Croft (Alicia Vikander), the fiercely independent daughter of a missing adventurer ,is driven to solve the puzzle of her father’s mysterious death on her own. Armed with only her sharp mind, blind faith and inherently stubborn spirit, Lara must learn to push herself beyond her limits as she braves the perilous journey into unknown lands, and ultimately earns the name tomb raider.','24.jpg',22.00,'MOVIE','Tomb Raider [Blu-ray] ','2018-07-25 13:13:15'),
(25,'2018-07-25 13:13:44','Jennifer Lawrence is Dominika, a former ballerina forced to enter Sparrow School, a secret government program that trains young recruits to manipulate, seduce and kill. She emerges as a dangerous agent, but is trapped in a world she desperately wants to escape. With the lives of her loved ones at risk, Dominika must find a way to take back control and serve justice to those who betrayed her.','25.jpg',25.00,'MOVIE','Red Sparrow','2018-07-25 13:13:54'),
(26,'2018-07-25 13:14:57','Robert Downey Jr, Chris Hemsworth, Mark Ruffalo, Chris Evans and Scarlett Johansson reprise their roles as the Marvel superheroes in this action sequel written and directed by Joss Whedon. In an attempt to bring peace to the world Tony Stark aka Iron Man (Downey Jr) creates Ultron (voice of James Spader), a robot with artificial intelligence.','26.jpg',18.00,'MOVIE','Avengers: Age of Ultron','2018-07-25 13:15:10'),
(27,'2018-07-25 13:15:46','In 1946, Peggy Carter is relegated to secretarial duties in the Strategic Scientific Reserve (SSR). When Howard Stark is accused of treason, he secretly recruits Peggy to clear his name with the help of his butler, Edwin Jarvis','27.jpg',22.00,'MOVIE','Marvel''s Agent Carter - Season 1','2018-07-25 13:15:57'),
(38,'2018-07-25 13:34:20','An unprecedented cinematic journey ten years in the making and spanning the entire Marvel Cinematic Universe, Marvel Studios'' "Avengers: Infinity War" brings to the screen the ultimate, deadliest showdown of all time. The Avengers and their Super Hero allies must be willing to sacrifice all in an attempt to defeat the powerful Thanos before his blitz of devastation and ruin puts an end to the universe.','38.jpg',12.99,'MOVIE','Avengers Infinity War','2018-07-25 13:34:38')
;


INSERT INTO `book` VALUES (2,1,1),(3,2,1),(4,3,2),(5,1,2),(6,1,1),(7,1,1),(8,1,1),(9,1,2),(10,2,1),(11,2,1),(12,2,1);


INSERT INTO `movie` VALUES (1,1),(19,1),(20,2),(21,2),(22,1),(23,2),(24,2),(25,1),(26,2),(27,2),(38,1);


INSERT INTO `movie_language` VALUES (1,3),(1,1),(19,1),(19,2),(19,3),(19,4),(20,3),(20,2),(21,1),(21,2),(21,4),(22,1),(22,3),(23,1),(23,2),(24,2),(24,3),(24,4),(25,1),(25,2),(25,4),(26,1),(26,3),(26,4),(27,1),(27,3),(27,4),(38,1),(38,2),(38,3),(38,4);
