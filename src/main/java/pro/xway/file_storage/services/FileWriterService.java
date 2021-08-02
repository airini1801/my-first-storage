package pro.xway.file_storage.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.xway.file_storage.dao.entities.User;
import pro.xway.file_storage.exception.ApplicationException;
import pro.xway.file_storage.exception.ExceptionEnum;

import java.io.*;

@Service
public class FileWriterService {
    private final UserService userService;

    public FileWriterService(UserService userService) {
        this.userService = userService;
    }

    public void write(MultipartFile file) {
        User currentUser = userService.getCurrentUser();
        String filename = file.getOriginalFilename();
        File fullFilePath = new File("./storage/" + currentUser.getId() + "/" + filename);
        File directoryPath = new File("./storage/" + currentUser.getId());
        try {
            if(!directoryPath.exists()){
                boolean isCreatedDirectory = directoryPath.mkdirs();
                if(!isCreatedDirectory){
                    System.out.println("Не получилось создать директорию");
                    throw new ApplicationException(ExceptionEnum.FILE_NOT_UPLOAD);
                }
            }
            boolean createdNewFile = fullFilePath.createNewFile();
            if (createdNewFile) {
                FileOutputStream fileOutputStream = new FileOutputStream(fullFilePath);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                bufferedOutputStream.write(file.getBytes());
                bufferedOutputStream.close();
            } else {
                throw new ApplicationException(ExceptionEnum.FILE_NOT_UPLOAD);
            }
        } catch (IOException e) {
            System.out.println("Вам не удалось загрузить " + filename + " => " + e.getMessage());
            throw new ApplicationException(ExceptionEnum.FILE_NOT_UPLOAD);
        }

    }
}
