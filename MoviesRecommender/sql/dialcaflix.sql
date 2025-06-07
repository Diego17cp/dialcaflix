create table users (
	id serial primary key,
	name varchar(100) not null,
	email varchar(100) unique not null,
	password text not null
);
create table movie (
	id serial primary key,
	title varchar(150) not null,
	genre varchar(100) not null,
	year int,
	description text, 
	poster_url text not null
);
create table rating (
	id serial primary key,
	user_id int references users(id) on delete cascade,
	movie_id int references movie(id) on delete cascade,
	rate int check (rate between 1 and 5),
	rating_date timestamp default current_timestamp,
	unique (user_id, movie_id)
);
-- Inserts para movies

INSERT INTO movie (title, genre, year, description, poster_url) VALUES
-- Acción
('The Dark Knight', 'Acción', 2008, 'Batman enfrenta al Joker en esta intensa película de superhéroes.', 'https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg'),
('Mad Max: Fury Road', 'Acción', 2015, 'Una frenética persecución en un mundo postapocalíptico.', 'https://image.tmdb.org/t/p/w500/8tZYtuWezp8JbcsvHYO0O46tFbo.jpg'),
('Gladiator', 'Acción', 2000, 'Un general romano busca venganza contra el emperador corrupto.', 'https://image.tmdb.org/t/p/w500/ty8TGRuvJLPUmAR1H1nRIsgwvim.jpg'),
('John Wick', 'Acción', 2014, 'Un exasesino regresa para vengar la muerte de su perro.', 'https://image.tmdb.org/t/p/w500/5vHssUeVe25bMrof1HyaPyWgaP.jpg'),
('Deadpool', 'Acción', 2016, 'Un antihéroe con sentido del humor negro busca justicia.', 'https://image.tmdb.org/t/p/w500/fSRb7vyIP8rQpL0I47P3qUsEKX3.jpg'),
('Inception', 'Acción', 2010, 'Un ladrón que roba secretos entrando en los sueños.', 'https://image.tmdb.org/t/p/w500/9gk7adHYeDvHkCSEqAvQNLV5Uge.jpg'),
('The Matrix', 'Acción', 1999, 'Un hacker descubre que la realidad es una simulación.', 'https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg'),
('Guardians of the Galaxy', 'Acción', 2014, 'Un grupo de inadaptados salva la galaxia.', 'https://image.tmdb.org/t/p/w500/y31QB9kn3XSudA15tV7UWQ9XLuW.jpg'),
('The Avengers', 'Acción', 2012, 'Los superhéroes más poderosos del mundo unen fuerzas.', 'https://image.tmdb.org/t/p/w500/RYMX2wcKCBAr24UyPD7xwmjaTn.jpg'),
('Black Panther', 'Acción', 2018, 'El rey de Wakanda protege su reino y al mundo.', 'https://image.tmdb.org/t/p/w500/uxzzxijgPIY7slzFvMotPv8wjKA.jpg'),

-- Drama
('Fight Club', 'Drama', 1999, 'Un hombre insomne forma un club de pelea clandestino.', 'https://image.tmdb.org/t/p/w500/bptfVGEQuv6vDTIMVCHjJ9Dz8PX.jpg'),
('The Shawshank Redemption', 'Drama', 1994, 'La esperanza y la amistad en una prisión.', 'https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg'),
('Forrest Gump', 'Drama', 1994, 'La vida extraordinaria de un hombre sencillo.', 'https://image.tmdb.org/t/p/w500/saHP97rTPS5eLmrLQEcANmKrsFl.jpg'),
('Whiplash', 'Drama', 2014, 'Un baterista joven enfrenta un duro instructor.', 'https://image.tmdb.org/t/p/w500/oPXd9u8IQZ84hU14Atlo1kPehPQ.jpg'),
('The Green Mile', 'Drama', 1999, 'La historia mágica en el corredor de la muerte.', 'https://image.tmdb.org/t/p/w500/velWPhVMQeQKcxggNEU8YmIo52R.jpg'),
('12 Years a Slave', 'Drama', 2013, 'La lucha de un hombre libre convertido en esclavo.', 'https://image.tmdb.org/t/p/w500/wh3xizTJvHYY04HyYt8MiC6POXW.jpg'),
('The Social Network', 'Drama', 2010, 'La creación de Facebook y sus conflictos legales.', 'https://image.tmdb.org/t/p/w500/2nNSOXZMYH6bJ0uWdAhf4ug1sPV.jpg'),
('The Imitation Game', 'Drama', 2014, 'La historia del matemático Alan Turing y la máquina Enigma.', 'https://image.tmdb.org/t/p/w500/Al8mhr3lJpwbGE9MtROzXsflDdc.jpg'),
('The Revenant', 'Drama', 2015, 'Un hombre lucha por sobrevivir y vengarse en la naturaleza.', 'https://image.tmdb.org/t/p/w500/oXUWEc5i3wYyFnL1Ycu8ppxxPvs.jpg'),
('La La Land', 'Drama', 2016, 'Un romance entre un músico y una actriz en Los Ángeles.', 'https://image.tmdb.org/t/p/w500/ylXCdC106IKiarftHkcacasaAcb.jpg'),

-- Comedia
('The Grand Budapest Hotel', 'Comedia', 2014, 'Las aventuras de un conserje y su protegido.', 'https://image.tmdb.org/t/p/w500/nX5XotM9yprCKarRH4fzOq1VM1J.jpg'),
('The Big Lebowski', 'Comedia', 1998, 'Un vago se ve envuelto en un secuestro.', 'https://image.tmdb.org/t/p/w500/k5T2cfubfXkIKCEM8QTXtuQhF9D.jpg'),
('Superbad', 'Comedia', 2007, 'Dos amigos en su último año de secundaria.', 'https://image.tmdb.org/t/p/w500/ek8e8txUyUwd2BNqj6lFEerJfbq.jpg'),
('Step Brothers', 'Comedia', 2008, 'Dos adultos obligados a vivir juntos.', 'https://image.tmdb.org/t/p/w500/ewBH1ZxvtVjVi5T7HzB1lUXQh9a.jpg'),
('Anchorman', 'Comedia', 2004, 'Un presentador de noticias egocéntrico.', 'https://image.tmdb.org/t/p/w500/7Y2e4WpjDFyxRZfMPa5W0u4ZhQn.jpg'),
('21 Jump Street', 'Comedia', 2012, 'Dos policías se infiltran en una secundaria.', 'https://image.tmdb.org/t/p/w500/8UXp81zCMbXn7aoyV6WgztH5mqX.jpg'),
('Zombieland', 'Comedia', 2009, 'Un apocalipsis zombie con mucho humor.', 'https://image.tmdb.org/t/p/w500/3K4tBgc0NQYVzyu2aFDxhLqV2eU.jpg'),
('The Hangover', 'Comedia', 2009, 'Una despedida de soltero descontrolada.', 'https://image.tmdb.org/t/p/w500/uluhlXubGu1VxU63X9VHCLWDAYP.jpg'),
('Crazy Rich Asians', 'Comedia', 2018, 'Una historia de amor con ricos y extravagantes.', 'https://image.tmdb.org/t/p/w500/ynXoOxmDHNQ4UAy0oU6avW71HVW.jpg'),
('Deadpool', 'Comedia', 2016, 'Un antihéroe con humor negro y mucha acción.', 'https://image.tmdb.org/t/p/w500/fSRb7vyIP8rQpL0I47P3qUsEKX3.jpg'),

-- Animación
('Finding Nemo', 'Animación', 2003, 'Un pez busca a su hijo perdido en el océano.', 'https://image.tmdb.org/t/p/w500/eUqF4BpnRu7v8KLzL8A0IElXG9H.jpg'),
('Up', 'Animación', 2009, 'Un anciano vuela a América del Sur en su casa con globos.', 'https://image.tmdb.org/t/p/w500/9V2C1rWcElxGhT6F8P6Xe6Z0riG.jpg'),
('The Lion King', 'Animación', 1994, 'La historia de Simba, el león heredero.', 'https://image.tmdb.org/t/p/w500/2bXbqYdUdNVa8VIWXVfclP2ICtT.jpg'),
('Toy Story', 'Animación', 1995, 'Los juguetes cobran vida cuando no hay humanos.', 'https://image.tmdb.org/t/p/w500/uXDfjJbdP4ijW5hWSBrPrlKpxab.jpg'),
('Inside Out', 'Animación', 2015, 'Las emociones de una niña cobran vida.', 'https://image.tmdb.org/t/p/w500/aAmfIX3TT40zUHGcCKrlOZRKC7u.jpg'),
('Coco', 'Animación', 2017, 'Un niño viaja al mundo de los muertos.', 'https://image.tmdb.org/t/p/w500/gGEsBPAijhVUFoiNpgZXqRVWJt2.jpg'),
('Zootopia', 'Animación', 2016, 'Una ciudad habitada por animales antropomórficos.', 'https://image.tmdb.org/t/p/w500/6K1x7JSXmTpYFvCE6qqSHbTFlqT.jpg'),
('Shrek', 'Animación', 2001, 'Un ogro vive aventuras para salvar a la princesa.', 'https://image.tmdb.org/t/p/w500/teHP2Ex54ZSAiGD6Zz6QUaDRnRu.jpg'),
('Frozen', 'Animación', 2013, 'Una princesa con poderes de hielo busca a su hermana.', 'https://image.tmdb.org/t/p/w500/7z9Xy7JKXcscgVYHPij04eEttqk.jpg'),
('How to Train Your Dragon', 'Animación', 2010, 'Un joven entrena a un dragón.', 'https://image.tmdb.org/t/p/w500/9lFKBtaVIhP7E2Pk0IY1CwTKTMZ.jpg'),

-- Fantasía
('Harry Potter and the Sorcerer''s Stone', 'Fantasía', 2001, 'Un niño descubre que es un mago.', 'https://image.tmdb.org/t/p/w500/8Q6iPPF0OYxjC04cqzDAbgOSu6b.jpg'),
('The Lord of the Rings: The Fellowship of the Ring', 'Fantasía', 2001, 'Un grupo emprende un viaje para destruir un anillo.', 'https://image.tmdb.org/t/p/w500/6oom5QYQ2yQTMJIbnvbkBL9cHo6.jpg'),
('Pan''s Labyrinth', 'Fantasía', 2006, 'Una niña encuentra un mundo mágico y oscuro.', 'https://image.tmdb.org/t/p/w500/8WnZGfCQ8pm3oSP1x4kRtBVG02J.jpg'),
('The Chronicles of Narnia: The Lion, the Witch and the Wardrobe', 'Fantasía', 2005, 'Niños descubren un mundo mágico y luchan contra la bruja.', 'https://image.tmdb.org/t/p/w500/dL11DBPcRhWWnJcFXl9A07MrqTI.jpg'),
('Stardust', 'Fantasía', 2007, 'Un joven busca una estrella caída.', 'https://image.tmdb.org/t/p/w500/7bq8d7tslFJw4SnGi6KXbsrZ2En.jpg'),
('The Hobbit: An Unexpected Journey', 'Fantasía', 2012, 'Un hobbit emprende un viaje inesperado.', 'https://image.tmdb.org/t/p/w500/6tfT03sGp9k4c0J7C4C6Jo8f00F.jpg'),
('Alice in Wonderland', 'Fantasía', 2010, 'Una joven cae en un mundo extraño y fantástico.', 'https://image.tmdb.org/t/p/w500/svQe6FduzNeFPU4jDT6jy6yFtm.jpg'),
('The Shape of Water', 'Fantasía', 2017, 'Un romance entre una mujer y una criatura acuática.', 'https://image.tmdb.org/t/p/w500/6wDlXtBfTQ0J2MwlYAb4VeB0y5W.jpg'),
('Stardust', 'Fantasía', 2007, 'Un joven busca una estrella caída y encuentra aventuras mágicas.', 'https://image.tmdb.org/t/p/w500/lCb6j0uWxM73vPwiLhKUL6rLz5l.jpg'),
('The Hobbit: An Unexpected Journey', 'Fantasía', 2012, 'Bilbo Bolsón emprende un viaje con enanos para recuperar su hogar.', 'https://image.tmdb.org/t/p/w500/oXUWEc5i3wYyFnL1Ycu8ppxxPvs.jpg'),
('Alice in Wonderland', 'Fantasía', 2010, 'Alicia regresa al país de las maravillas para una aventura.', 'https://image.tmdb.org/t/p/w500/c9dEW7eT2MZu2SCbq9JZZnrfHbA.jpg'),
('The Shape of Water', 'Fantasía', 2017, 'Un romance entre una limpiadora muda y una criatura anfibia.', 'https://image.tmdb.org/t/p/w500/w6HwB6MS8O75hmT1i6fCGLUpR3M.jpg'),
('Coraline', 'Fantasía', 2009, 'Una niña descubre un mundo paralelo extraño y peligroso.', 'https://image.tmdb.org/t/p/w500/w8UyRkQGbBcdD6PA1rbEbQPPrlT.jpg'),

-- Ciencia Ficción
('Interstellar', 'Ciencia Ficción', 2014, 'Un viaje a través del espacio y tiempo para salvar la humanidad.', 'https://image.tmdb.org/t/p/w500/rAiYTfKGqDCRIIqo664sY9XZIvQ.jpg'),
('Blade Runner 2049', 'Ciencia Ficción', 2017, 'Un replicante busca respuestas en un futuro distópico.', 'https://image.tmdb.org/t/p/w500/aMpyrCizvSdc0UIMblJ1srVgAEF.jpg'),
('Arrival', 'Ciencia Ficción', 2016, 'Una lingüista intenta comunicarse con alienígenas.', 'https://image.tmdb.org/t/p/w500/x2FJsf1ElAgr63Y3PNPtJrcmp1k.jpg'),
('Ex Machina', 'Ciencia Ficción', 2014, 'Un programador prueba la inteligencia artificial de un robot.', 'https://image.tmdb.org/t/p/w500/3jcbDmRFiQ83drXNOvRDeKHxS0C.jpg'),
('Gravity', 'Ciencia Ficción', 2013, 'Una astronauta lucha por sobrevivir en el espacio.', 'https://image.tmdb.org/t/p/w500/6ZbiZpUtI0pQWCMWX8R6upwPf6m.jpg'),
('The Martian', 'Ciencia Ficción', 2015, 'Un astronauta queda varado en Marte y lucha por sobrevivir.', 'https://image.tmdb.org/t/p/w500/5aGhaIHYuQbqlHWvWYqMCnj40y2.jpg'),
('District 9', 'Ciencia Ficción', 2009, 'Un extraterrestre y humano se unen en Sudáfrica.', 'https://image.tmdb.org/t/p/w500/fWgLxy8YBdT0ilRZ03sMtbTlzC5.jpg'),
('Avatar', 'Ciencia Ficción', 2009, 'Un marine en Pandora se une a la tribu local.', 'https://image.tmdb.org/t/p/w500/kmcqlZGaSh20zpTbuoF0F4ShERa.jpg'),
('Edge of Tomorrow', 'Ciencia Ficción', 2014, 'Un soldado revive el mismo día de batalla una y otra vez.', 'https://image.tmdb.org/t/p/w500/7qop80YfuO0BwJa1uXk1DXUUEwv.jpg'),
('Minority Report', 'Ciencia Ficción', 2002, 'Un policía usa la precognición para atrapar criminales.', 'https://image.tmdb.org/t/p/w500/3p9f6vCqcTL3UcnYCuWgI0k84Jy.jpg'),

-- Terror
('The Conjuring', 'Terror', 2013, 'Investigadores paranormales enfrentan un caso aterrador.', 'https://image.tmdb.org/t/p/w500/pFlaoHTZeyNkG83vxsAJiGzfSsa.jpg'),
('Get Out', 'Terror', 2017, 'Un hombre descubre un secreto aterrador en la familia de su novia.', 'https://image.tmdb.org/t/p/w500/xJWPZIYOEFIjZpBL7SVBGnzRYXp.jpg'),
('It', 'Terror', 2017, 'Un payaso maldito aterroriza a un grupo de niños.', 'https://image.tmdb.org/t/p/w500/w9kR8qbmQ01HwnvK4alvnQ2ca0L.jpg'),
('A Quiet Place', 'Terror', 2018, 'Una familia debe vivir en silencio para sobrevivir.', 'https://image.tmdb.org/t/p/w500/nAU74GmpUk7t5iklEp3bufwDq4n.jpg'),
('Hereditary', 'Terror', 2018, 'Una familia enfrenta sucesos terroríficos tras una muerte.', 'https://image.tmdb.org/t/p/w500/2Fxmhks0bxGSBdJ92vM42m3tFK.jpg'),
('The Babadook', 'Terror', 2014, 'Una madre y su hijo son acosados por una criatura oscura.', 'https://image.tmdb.org/t/p/w500/7KXJf5Li2vZ6Y24X8xl2v4ME9Pz.jpg'),
('Midsommar', 'Terror', 2019, 'Un festival pagano con secretos muy oscuros.', 'https://image.tmdb.org/t/p/w500/hU0E130tsGdsYa4K9lc3Xrn5Wyt.jpg'),
('Insidious', 'Terror', 2010, 'Una familia enfrenta demonios que intentan poseer a su hijo.', 'https://image.tmdb.org/t/p/w500/6E6E20P5dp7I3MGZmGoj3iZTC2A.jpg'),
('The Ring', 'Terror', 2002, 'Una cinta de video maldita causa muertes extrañas.', 'https://image.tmdb.org/t/p/w500/6vS9Y4D6XWHX0w3MLGQoM5MLp9g.jpg'),
('Sinister', 'Terror', 2012, 'Un escritor descubre videos terroríficos en su nueva casa.', 'https://image.tmdb.org/t/p/w500/6y0tNyL2JQjvAh4U1wqtjWYZ2pV.jpg'),

-- Thriller
('Se7en', 'Thriller', 1995, 'Dos detectives persiguen a un asesino que basa sus crímenes en los pecados capitales.', 'https://image.tmdb.org/t/p/w500/69Sns8WoET6CfaYlIkHbla4l7nC.jpg'),
('Gone Girl', 'Thriller', 2014, 'La desaparición de una mujer desata sospechas y secretos.', 'https://image.tmdb.org/t/p/w500/8N75IQyUBrjNi9gHhVXrYFt5v84.jpg'),
('Shutter Island', 'Thriller', 2010, 'Un detective investiga la desaparición de una paciente en un hospital psiquiátrico.', 'https://image.tmdb.org/t/p/w500/9yBVqNruk6Ykrwc32qrK2TIE5xw.jpg'),
('Prisoners', 'Thriller', 2013, 'Un padre secuestra al hombre que cree responsable de la desaparición de su hija.', 'https://image.tmdb.org/t/p/w500/auxAFb4bCMF3jlwFSBjoRkJbdfz.jpg'),
('The Silence of the Lambs', 'Thriller', 1991, 'Una agente del FBI busca la ayuda de un asesino caníbal para atrapar a otro.', 'https://image.tmdb.org/t/p/w500/rplLJ2hPcOQmkFhTqUte0MkEaO2.jpg'),
('Nightcrawler', 'Thriller', 2014, 'Un hombre se adentra en el periodismo sensacionalista nocturno.', 'https://image.tmdb.org/t/p/w500/wL5P4x3Oy60LkuHHc3jvzybZBVF.jpg'),
('Black Swan', 'Thriller', 2010, 'Una bailarina sufre una crisis emocional mientras prepara un papel.', 'https://image.tmdb.org/t/p/w500/5EYMGiE2a4f96q6YbzMI4y42hL1.jpg'),
('The Girl with the Dragon Tattoo', 'Thriller', 2011, 'Un periodista y una hacker investigan una desaparición misteriosa.', 'https://image.tmdb.org/t/p/w500/1d0Jv98qKdoQkZFFaQxA0uK1p2i.jpg'),
('Oldboy', 'Thriller', 2003, 'Un hombre es secuestrado y busca venganza tras 15 años.', 'https://image.tmdb.org/t/p/w500/7J1jyXL91fWOMbYzWzP3lG3DDaE.jpg'),
('Fight Club', 'Thriller', 1999, 'Un hombre crea un club clandestino de peleas para escapar de su vida aburrida.', 'https://image.tmdb.org/t/p/w500/bptfVGEQuv6vDTIMVCHjJ9Dz8PX.jpg'),

-- Comedia
('The Hangover', 'Comedia', 2009, 'Un grupo de amigos pierde al novio en Las Vegas tras una noche loca.', 'https://image.tmdb.org/t/p/w500/uluhlXubGu1VxU63X9VHCLWDAYP.jpg'),
('Superbad', 'Comedia', 2007, 'Dos adolescentes intentan conseguir alcohol para una fiesta épica.', 'https://image.tmdb.org/t/p/w500/ek8e8txUyUwd2BNqj6lFEerJfbq.jpg'),
('Step Brothers', 'Comedia', 2008, 'Dos hombres inmaduros se convierten en hermanastros.', 'https://image.tmdb.org/t/p/w500/akbX0TNyF1g7SIn8bTIc1pJ8CyH.jpg'),
('Anchorman', 'Comedia', 2004, 'Un presentador de noticias lucha por mantenerse en la cima.', 'https://image.tmdb.org/t/p/w500/7J0hf9yqJZIcE7M99wZbQO1RQWw.jpg'),
('Zoolander', 'Comedia', 2001, 'Un modelo perdido intenta salvar su carrera.', 'https://image.tmdb.org/t/p/w500/f7XbHNjvhzjQ6sqZjz6u5w1FxgE.jpg'),
('Tropic Thunder', 'Comedia', 2008, 'Un grupo de actores se enreda en una guerra real.', 'https://image.tmdb.org/t/p/w500/q1D28j5pIJzvT4zB0pIK51AxlxA.jpg'),
('Pineapple Express', 'Comedia', 2008, 'Un testigo de un crimen y su dealer huyen de criminales.', 'https://image.tmdb.org/t/p/w500/dq9mkb6lnxKhdNt2vraxa6sJpXQ.jpg'),
('Dumb and Dumber', 'Comedia', 1994, 'Dos amigos muy tontos hacen un viaje ridículo.', 'https://image.tmdb.org/t/p/w500/qS6Jm7xMlN9vA2UDnEsEy2MeYYn.jpg'),
('Ghostbusters', 'Comedia', 1984, 'Un grupo de científicos combate fantasmas en Nueva York.', 'https://image.tmdb.org/t/p/w500/k1QhoWxl6ALy6NQgzxvdlmf1xDu.jpg'),
('The Grand Budapest Hotel', 'Comedia', 2014, 'Las aventuras de un conserje y su protegido.', 'https://image.tmdb.org/t/p/w500/nX5XotM9yprCKarRH4fzOq1VM1J.jpg'),

-- Animación
('Toy Story', 'Animación', 1995, 'Los juguetes cobran vida cuando no están los humanos.', 'https://image.tmdb.org/t/p/w500/uXDfjJbdP4ijW5hWSBrPrlKpxab.jpg'),
('Finding Nemo', 'Animación', 2003, 'Un pez busca a su hijo perdido en el océano.', 'https://image.tmdb.org/t/p/w500/eHu49rL1f1O0ahz2wJ2T3x2kO4x.jpg'),
('The Lion King', 'Animación', 1994, 'El joven león Simba busca su lugar en la sabana.', 'https://image.tmdb.org/t/p/w500/sKCr78MXSLixwmZ8DyJLrpMsd15.jpg'),
('Spirited Away', 'Animación', 2001, 'Una niña entra a un mundo mágico para rescatar a sus padres.', 'https://image.tmdb.org/t/p/w500/oRvMaJOmapypFUcQqpgHMZA6qL9.jpg'),
('Frozen', 'Animación', 2013, 'Dos hermanas enfrentan poderes mágicos y un invierno eterno.', 'https://image.tmdb.org/t/p/w500/yp8vEZflGynljI9Q7mWy9KbxMu2.jpg'),
('Inside Out', 'Animación', 2015, 'Las emociones de una niña se enfrentan en su mente.', 'https://image.tmdb.org/t/p/w500/aAmfIX3TT40zUHGcCKrlOZRKC7u.jpg'),
('Despicable Me', 'Animación', 2010, 'Un villano con corazón blando adopta tres niñas.', 'https://image.tmdb.org/t/p/w500/qmDpIHrmpJINaRKAfWQfftjCdyi.jpg'),
('Shrek', 'Animación', 2001, 'Un ogro vive aventuras para rescatar a una princesa.', 'https://image.tmdb.org/t/p/w500/6u1fYtxG5eqjhtCPDx04pJphQRW.jpg'),
('Coco', 'Animación', 2017, 'Un niño viaja al mundo de los muertos para descubrir su legado.', 'https://image.tmdb.org/t/p/w500/eKi8dIrr8voobbaGzDpe8w0PVbC.jpg'),
('Moana', 'Animación', 2016, 'Una joven navegante busca salvar a su pueblo.', 'https://image.tmdb.org/t/p/w500/nmGWzTLMXy9x7mKd8NKPLmHtWGa.jpg');

