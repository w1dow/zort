import os
import random
import string

# randomises file name

def random_name(length=8):
    chars = string.ascii_letters + string.digits
    return ''.join(random.choice(chars) for _ in range(length))

cwd = os.getcwd()

for filename in os.listdir(cwd):
    if os.path.isfile(filename) and filename != os.path.basename(__file__):
        name, ext = os.path.splitext(filename)
        new_name = random_name() + ext
        while os.path.exists(new_name):
            new_name = random_name() + ext
        os.rename(filename, new_name)

