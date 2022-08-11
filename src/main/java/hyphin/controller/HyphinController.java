package hyphin.controller;

import hyphin.model.Login;
import hyphin.model.User;
import hyphin.repository.CustomUserRepository;
import hyphin.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;


/**
 * Created by Abhishek Satsangi on 27/06/2022
 */
@RestController
public class HyphinController {

    private static final Logger LOGGER = LogManager.getLogger(HyphinController.class);

    private RestTemplate restTemplate;

    @Autowired
    CustomUserRepository customUserRepository;

    @Autowired
    UserService userService;

    @ModelAttribute(value = "register")
    public User newUser()
    {
        return new User();
    }

    @ModelAttribute(value = "login")
    public Login newLogin()
    {
        return new Login();
    }

    @GetMapping("/")
    public ModelAndView viewHome() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        return mav;
    }

    @GetMapping("/index")
    public ModelAndView viewIndex() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        return mav;
    }

    @GetMapping("/faqs")
    public ModelAndView viewFaqs() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("faqs");
        return mav;
    }

    @GetMapping("/start")
    public ModelAndView viewStart() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("start");
        return mav;
    }

    @GetMapping("/cookies")
    public ModelAndView viewCookies() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("cookies");
        return mav;
    }

    @GetMapping("/terms")
    public ModelAndView viewTerms() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("terms");
        return mav;
    }

    @GetMapping("/privacy")
    public ModelAndView viewPrivacy() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("privacy");
        return mav;
    }

    @GetMapping("/1")
    public ModelAndView viewCourses() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("1");
        return mav;
    }

    @GetMapping("/glossary")
    public ModelAndView viewGlossary() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("glossary");
        return mav;
    }

    @GetMapping("/search")
    public ModelAndView viewSearch() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("search");
        return mav;
    }

    @GetMapping("/2")
    public ModelAndView view2ndPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("2");
        return mav;
    }

    @GetMapping("/3")
    public ModelAndView view3rdPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("3");
        return mav;
    }

    @GetMapping("/4")
    public ModelAndView view4thPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("4");
        return mav;
    }

    @GetMapping("/5_8")
    public ModelAndView view5thPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("5_8");
        return mav;
    }

    @GetMapping("/9")
    public ModelAndView view9thPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("9");
        return mav;
    }

    @GetMapping("/10")
    public ModelAndView view10thPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("10");
        return mav;
    }

    @GetMapping("/14")
    public ModelAndView view14thPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("14");
        return mav;
    }

    @GetMapping("/account")
    public ModelAndView viewAccountPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("account");
        return mav;
    }

    @GetMapping("/assets")
    public ModelAndView viewAssetsPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("assets");
        return mav;
    }

    @GetMapping("/blockchain")
    public ModelAndView viewBlockChain() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("blockchain");
        return mav;
    }

    @GetMapping("/cancel")
    public ModelAndView viewCancel() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("cancel");
        return mav;
    }

    @GetMapping("/commodities")
    public ModelAndView viewCommodities() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("commodities");
        return mav;
    }

    @GetMapping("/compliance")
    public ModelAndView viewCompliance() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("compliance");
        return mav;
    }

    @GetMapping("/conduct-risk")
    public ModelAndView viewConductRisk() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("conduct-risk");
        return mav;
    }

    @GetMapping("/credit")
    public ModelAndView viewCredit() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("credit");
        return mav;
    }

    @GetMapping("/cryptocurrency")
    public ModelAndView viewCryptoCurrency() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("cryptocurrency");
        return mav;
    }

    @GetMapping("/digital-assets")
    public ModelAndView viewDigitalAssets() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("digital-assets");
        return mav;
    }

    @GetMapping("/ec-cfd-1")
    public ModelAndView viewCFD1() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("ec-cfd-1");
        return mav;
    }

    @GetMapping("/ec-cfd-2")
    public ModelAndView viewCFD2() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("ec-cfd-2");
        return mav;
    }

    @GetMapping("/ec-cfd-3")
    public ModelAndView viewCFD3() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("ec-cfd-3");
        return mav;
    }

    @GetMapping("/ec-cfd-4a")
    public ModelAndView viewCFD4() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("ec-cfd-4a");
        return mav;
    }

    @GetMapping("/ec-cfd-4b")
    public ModelAndView viewCFD4b() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("ec-cfd-4b");
        return mav;
    }

    @GetMapping("/ec-cfd-4c")
    public ModelAndView viewCFD4c() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("ec-cfd-4c");
        return mav;
    }

    @GetMapping("/ec-cfd-5a")
    public ModelAndView ECCFD5a() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("ec-cfd-5a");
        return mav;
    }

    @GetMapping("/ec-cfd-5c")
    public ModelAndView ECCFD5c() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("ec-cfd-5c");
        return mav;
    }

    @GetMapping("/ec-cfd-5d")
    public ModelAndView ECCFD5d() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("ec-cfd-5d");
        return mav;
    }

    @GetMapping("/ec-cfd-5e")
    public ModelAndView ECCFD5() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("ec-cfd-5e");
        return mav;
    }

    @GetMapping("/ec-cfd-6")
    public ModelAndView ECCFD6() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("ec-cfd-6");
        return mav;
    }

    @GetMapping("/ec-cfd-6a")
    public ModelAndView ECCFD6a() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("ec-cfd-6a");
        return mav;
    }

    @GetMapping("/ec-cfd-7a")
    public ModelAndView ECCFD7a() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("ec-cfd-7a");
        return mav;
    }

    @GetMapping("/ec-cfd-7b")
    public ModelAndView viewECCFD7b() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("ec-cfd-7b");
        return mav;
    }

    @GetMapping("/ec-cfd-7c")
    public ModelAndView viewECCFD7() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("ec-cfd-7c");
        return mav;
    }

    @GetMapping("/ec-cfd-8a")
    public ModelAndView viewECCFD8() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("ec-cfd-8a");
        return mav;
    }

    @GetMapping("/ec-cfd-8b")
    public ModelAndView viewECCFD8b() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("ec-cfd-8b");
        return mav;
    }

    @GetMapping("/equities")
    public ModelAndView viewEquities() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("equities");
        return mav;
    }

    @GetMapping("/financial-crime")
    public ModelAndView viewFinancialCrime() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("financial-crime");
        return mav;
    }

    @GetMapping("/financial-literacy")
    public ModelAndView viewFinancialLiteracy() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("financial-literacy");
        return mav;
    }

    @GetMapping("/financial-products")
    public ModelAndView viewFinancialProducts() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("financial-products");
        return mav;
    }

    @GetMapping("/fixed-income")
    public ModelAndView viewFixedIncome() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("fixed-income");
        return mav;
    }

    @GetMapping("/forgotten-password")
    public ModelAndView viewForgottenPassword() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("forgotten-password");
        return mav;
    }

    @GetMapping("/fp-credit-products")
    public ModelAndView viewCreditProducts() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("fp-credit-products");
        return mav;
    }

    @GetMapping("/fp-fundamentals")
    public ModelAndView viewFundamentals() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("fp-fundamentals");
        return mav;
    }

    @GetMapping("/fp-investment-products")
    public ModelAndView viewProducts() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("fp-investment-products");
        return mav;
    }

    @GetMapping("/fp-mortgages")
    public ModelAndView viewMortgages() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("fp-mortgages");
        return mav;
    }

    @GetMapping("/fp-pensions")
    public ModelAndView viewFPPension() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("fp-pensions");
        return mav;
    }

    @GetMapping("/fp-savings")
    public ModelAndView viewFPSavings() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("fp-savings");
        return mav;
    }


    @PostMapping("/Done") public ModelAndView viewDone() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("4");
        return mav;
    }

    @GetMapping("/investment-strategy")
    public ModelAndView viewInvestmentStrategy() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("investment-strategy");
        return mav;
    }

    @GetMapping("/is-macroeconomics")
    public ModelAndView viewMacroEconomics() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("is-macroeconomics");
        return mav;
    }

    @GetMapping("/is-technical-analysis")
    public ModelAndView viewTechnicalAnalysis() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("is-technical-analysis");
        return mav;
    }

    @GetMapping("/structured-products")
    public ModelAndView viewStructuredProducts() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("structured-products");
        return mav;
    }

    @GetMapping("/surveillance")
    public ModelAndView viewSurveillance() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("surveillance");
        return mav;
    }

}
