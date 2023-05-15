package com.knpharm.knadmin.web;

import com.knpharm.knadmin.dto.PopupDto;
import com.knpharm.knadmin.dto.ProductDto;
import com.knpharm.knadmin.service.product.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/product")
public class ProductController {

    private Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/list/{brandCode}")
    public  String list(@PathVariable("brandCode") String brandCode, Model model) throws Exception {

        List<ProductDto> productList = productService.selectProductList(brandCode);

        model.addAttribute("menuCode", "product");
        model.addAttribute("brandCode", brandCode);

        model.addAttribute("productList", productList);

        return "/product/list";
    }

    @RequestMapping(value = "/edit/{brandCode}/{productName}")
    public  String edit(@PathVariable("brandCode") String brandCode, @PathVariable("productName") String productName, Model model) throws Exception {

        ProductDto product = productService.selectProduct(productName);

        model.addAttribute("menuCode", "product");
        model.addAttribute("brandCode", brandCode);
        model.addAttribute("productName", productName);
        model.addAttribute("product", product);

        return "/product/edit";
    }

    @ResponseBody
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public Map<String, Object> modify(
            ProductDto product,@RequestParam("productFile") MultipartFile[] files ) throws Exception {

        Map<String, Object> rtnObj = new HashMap<>();


        if(files.length < 0) {
            rtnObj.put("result", "fail");
            rtnObj.put("message", "잘못된 접근입니다.");
            return rtnObj;
        } else {
            MultipartFile productFile = files[0];
            if(!productFile.isEmpty()) {
                String originName = productFile.getOriginalFilename();//파일이름
                String exten = originName.substring(originName.lastIndexOf("."),originName.length());//확장자
                String random = "";//랜덤값
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HHmmssSSS");
                int rndNum = (int)(Math.random()*100000);
                random = sdf.format(new Date(System.currentTimeMillis()))+"_"+rndNum+exten;
                productFile.transferTo(new File(random));

                product.setProductOrgFileName(originName);
                product.setProductSaveFileName(random);
            }
        }

        logger.info("product -> " + product.toString());

        productService.updateProduct(product);

        rtnObj.put("result", "success");
        return rtnObj;
    }

}
