import zipfile
import os


def backup_to_zip(folder):
    # TODO: Backup the entire contents of "folder" into a ZIP file.
    folder0 = folder
    folder = os.path.abspath(folder)

    # TODO: Figure out the filename this code should use based on what files already exist.
    number = 1
    while True:
        zipfilename = os.path.basename(folder) + '-' + str(number) + '.zip'

        if not os.path.exists(zipfilename):
            break

        number = number + 1

    # TODO: Create the ZIP file.
    print('Creating %s...' % zipfilename)
    backup_zip = zipfile.ZipFile(zipfilename, 'w')

    # TODO: Walk the entire folder tree and compress the files in each folder.
    for foldername, subfolders, filenames in os.walk(folder0):
        print('Adding files in %s...' % foldername)

        # TODO: Add the current folder to the ZIP file.
        backup_zip.write(foldername)

        for filename in filenames:
            newBase = os.path.basename(folder) + '_'
            if filename.startswith(newBase) and filename.endswith('.zip'):
                continue
            backup_zip.write(os.path.join(foldername, filename))

    # TODO: Walk the entire folder tree and compress the files in each folder.
    print('Done.')


backup_to_zip('data')
