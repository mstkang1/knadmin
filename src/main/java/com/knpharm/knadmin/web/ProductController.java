package com.knpharm.knadmin.web;

import com.knpharm.knadmin.dto.ProductDto;
import com.knpharm.knadmin.service.product.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/product")
public class ProductController {

    private Logger logger = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping(value = "/list")
    public  String banner(Model model) {
        return "/product/list";
    }

    @RequestMapping(value = "/edit")
    public  String modify(Model model) {
        return "/product/edit";
    }

    @Autowired
    private ProductService productService;

    @ResponseBody
    @RequestMapping(value = "/productList", method = RequestMethod.POST)
    public Map<String, Object> productList(@RequestBody ProductDto productDto) throws Exception {

        Map<String, Object> rtnObj = new HashMap<>();

        List<ProductDto> productList = productService.selectProductList(productDto.getBrandCode());
        //logger.info("productList -> " + productList.toString());

        rtnObj.put("productList", productList);
        return rtnObj;
    }
}
