package com.example.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.server.mapper.BooksMapper;
import com.example.server.pojo.Books;
import com.example.server.pojo.RespBean;
import com.example.server.service.IBooksService;
import com.example.server.utils.FdfsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hanbing
 * @since 2022-11-20
 */
@RestController
@CrossOrigin
@RequestMapping("/books")
public class BooksController {
    @Autowired
    private BooksMapper booksMapper;
    @Autowired
    private IBooksService booksService;

    @PostMapping("upFile")
    public RespBean upFile(@RequestParam MultipartFile file) {
        String name = file.getName();
        String type = name.substring(name.lastIndexOf("."));
        if (type.equals("txt") || type.equals("doc") || type.equals("epub")) {
            String upload = FdfsUtils.upload(file);
            return RespBean.success("上传成功", FdfsUtils.URL + upload);
        } else {
            return RespBean.error("上传类型错误,上传失败");
        }
    }

    @GetMapping("downFile")
    public void downloadFile(Integer id, HttpServletResponse response) throws Exception {
        booksService.downloadFile(id, response);
    }

    @PostMapping("add")
    public RespBean add(@RequestBody Books books) {
        books.setId(null);
        if (booksMapper.insert(books) > 0) {
            return RespBean.success("上传成功");
        } else {
            return RespBean.error("上传失败");
        }
    }

    @DeleteMapping("delete")
    public RespBean deleteById(Integer id) {
        if (booksMapper.deleteById(id) > 0) {
            return RespBean.success("删除成功");
        } else {
            return RespBean.error("删除失败");
        }
    }

    @GetMapping("getAll")
    public RespBean getAll() {
        return RespBean.success("查询成功", booksMapper.selectList(null));
    }

    @GetMapping("likeAndPage/{name}")
    public RespBean like(@PathVariable("name") String name) {
        QueryWrapper<Books> booksQueryWrapper = new QueryWrapper<>();
        booksQueryWrapper.like("name", name);
        return RespBean.success("查询成功", booksMapper.selectList(booksQueryWrapper));
    }

    @GetMapping("likeAndPage/{page}/{size}")
    public RespBean page(@PathVariable("page") Integer page,
                         @PathVariable("size") Integer size) {
        Page<Books> booksPage = new Page<>(page, size);
        return RespBean.success("查询成功", booksMapper.selectPage(booksPage, null));
    }

    @GetMapping("likeAndPage/{name}/{page}/{size}")
    public RespBean likeAndPage(@PathVariable("name") String name,
                                @PathVariable("page") Integer page,
                                @PathVariable("size") Integer size) {
        Page<Books> booksPage = new Page<>(page, size);
        QueryWrapper<Books> booksQueryWrapper = new QueryWrapper<>();
        booksQueryWrapper.like("name", name);
        return RespBean.success("查询成功", booksMapper.selectPage(booksPage, booksQueryWrapper));
    }


}
