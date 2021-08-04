package pro.xway.file_storage.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.xway.file_storage.dto.CategoryDto;
import pro.xway.file_storage.dao.entities.Category;

import pro.xway.file_storage.exception.ApplicationException;
import pro.xway.file_storage.exception.ExceptionEnum;
import pro.xway.file_storage.services.CategoryService;
import pro.xway.file_storage.services.FileWriterService;

import java.util.Optional;


@Controller
@RequestMapping("/")
public class MainController implements Urls {
    private final CategoryService categoryService;
    private final FileWriterService fileWriterService;

    public MainController(CategoryService categoryService, FileWriterService fileWriterService) {
        this.categoryService = categoryService;
        this.fileWriterService = fileWriterService;
    }

    public static void main(String[] args) {
        System.out.println();
    }


    @GetMapping()
    public String index(Model model) {
        model.addAttribute("categories", categoryService.getCategoriesByUser());
        return "index";
    }

    @GetMapping(CATEGORY + "/{categoryId}")
    public String indexWithCategory(Model model, @PathVariable Long categoryId) {
        try {
            Category currantCategory = categoryService.getCategoryById(categoryId);
            model.addAttribute("categories", categoryService.getCategoriesByUser());
            model.addAttribute("currantCategory", currantCategory);
            return "index";
        } catch (ApplicationException e) {
            e.printStackTrace();
            return REDIRECT + EXCEPTION + e.getException().name();
        }

    }

    @PostMapping(CATEGORY + "/{categoryId}/upload")
    public String indexWithCategoryUpload(@RequestParam("file") MultipartFile file,
                                          @PathVariable Long categoryId) {
        if (!file.isEmpty()) {
            try {
                fileWriterService.write(file);
                return REDIRECT + CATEGORY + "/" + categoryId;
            } catch (ApplicationException e) {
                return REDIRECT + EXCEPTION + e.getException().name();
            }
        } else {
            return REDIRECT + EXCEPTION + ExceptionEnum.FILE_NOT_FOUND.name();
        }
    }


    @GetMapping(EXCEPTION + "{exceptionName}")
    public String indexWithException(Model model,
                                     @PathVariable String exceptionName) {
        Optional<ExceptionEnum> exceptionEnum = ExceptionEnum.search(exceptionName);
        if (exceptionEnum.isPresent()) {
            ExceptionEnum exception = exceptionEnum.get();
            model.addAttribute("error", exception.getMessage());
        }
        model.addAttribute("categories", categoryService.getCategoriesByUser());

        return "index";
    }


    @PostMapping(CATEGORY + "/save")
    public String save(@ModelAttribute CategoryDto createRequestDto) {
        categoryService.createAndSave(createRequestDto);
        return REDIRECT + MAIN;
    }

    @PostMapping(CATEGORY + "/update")
    public String update(@ModelAttribute Category category) {
        categoryService.save(category);
        return REDIRECT + CATEGORY + "/" + category.getId();
    }

    @PostMapping(CATEGORY + "/delete")
    public String delete(@ModelAttribute Category category) {
        categoryService.delete(category);
        return REDIRECT + MAIN;
    }


}
