package org.xxz.webuploader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.xxz.util.UploaderUtil;
import org.xxz.util.UploaderUtil.UploadResult;

@Controller
public class UploaderController {
    
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ModelAndView upload(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView m = new ModelAndView();
        UploadResult result = UploaderUtil.uploader(request, "/data", "filenameUploader", "/uploader", null, 1024 * 1024);
        m.addObject("result", result);
        return m;
    }
    
}
