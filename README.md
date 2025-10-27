# ZyBooksAutoComplete

Hi! If you've stumbled upon this repository, I assume you are equally as frustrated with ZyBooks as I was.

As of October 27, 2025, this program will complete every single question assigned to you in ZyBooks.

### How is this possible?

ZyBooks doesn't actually do any server side validation. There is a specific webrequest that can be sent which
will tell it that the problem is complete. There is some fancy checksum stuff in my code, but it turns out you
don't need it! The checksums aren't actually, well, checked.

### How can I use it?

Well... As much as I would like to provide step by step instructions here, the point of ZyBooks and school in 
general is to learn. I completed this project in Java because it was a fun way to practice my Java skills, 
while also getting the ZyBooks problems out of the way. I already know the material here.

So if you want to use my program, you are at least going to have to go through the effort of:
 - figuring out how to compile/run it
 - finding your ZyBooks auth_token

For extra credit, do some research into JSON web tokens, and decode yours here https://www.jwt.io/
These little tokens are found everywhere around the internet, and can be easily identified, and decoded.

Have fun!
