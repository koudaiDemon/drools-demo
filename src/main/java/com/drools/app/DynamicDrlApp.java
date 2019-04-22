package com.drools.app;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.drools.compiler.kie.builder.impl.KieFileSystemImpl;
import org.drools.compiler.kie.builder.impl.MemoryKieModule;
import org.kie.api.builder.*;
import org.kie.api.io.Resource;
import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.kie.api.KieServices;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * @author cWww
 * @Title
 * @Description
 * @date: 2019/4/16  14:44
 */
public class DynamicDrlApp {

    private static final String RULES_FILE_NAME = "rules.drl";

    /**
     * 默认规则文件所在路径
     */
    private static final String RULES_PATH = "base";

    /**
     * 虚拟文件路径
     */
    private static final String DROOLS_BASE_PATH = "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar;

    /**
     * 规则文件内容（可以从数据库中加载）
     */
    private static final String RULES = "package com.rules\n" +
            "\n" +
            "rule \"chapter4\"\n" +
            "\n" +
            "when\n" +
            "\n" +
            "then\n" +
            "\n" +
            "System.out.println(\"Fire the default rules for dynamic!\");\n" +
            "end";

    private KieContainer kieContainer;

    private ReleaseId releaseId;

    private KieRepository repository;

    {
        KieServices kieServices = KieServices.Factory.get();

        // 指定kjar包
        final ReleaseId releaseId = kieServices.newReleaseId("com.drools", "drools-demo1", "1.0-SNAPSHOT");

        // 创建初始化的kjar
        InternalKieModule kJar = initKieJar(kieServices, releaseId);
        KieRepository repository = kieServices.getRepository();
        repository.addKieModule(kJar);
        this.kieContainer = kieServices.newKieContainer(releaseId);
        this.releaseId = releaseId;
        this.repository = repository;
    }


    public KieContainer update(){
        KieServices kieServices = KieServices.Factory.get();
        //新增一个规则文件
        InternalKieModule kJar = createKieJar(kieServices, this.releaseId, new ResourceWrapper(ResourceFactory.newByteArrayResource(RULES.getBytes()), RULES_FILE_NAME),"base1.drl");
        this.repository.addKieModule(kJar);
        this.kieContainer.updateToVersion(releaseId);
        return kieContainer;
    }

    public KieContainer getKieContainer() {
        return kieContainer;
    }

    public void setKieContainer(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

    public static void main(String[] args) {
        KieServices kieServices = KieServices.Factory.get();

        // 指定kjar包
        final ReleaseId releaseId = kieServices.newReleaseId("com.drools", "drools-demo1", "1.0-SNAPSHOT");

        // 创建初始化的kjar
        InternalKieModule kJar = initKieJar(kieServices, releaseId);
        KieRepository repository = kieServices.getRepository();
        repository.addKieModule(kJar);
        KieContainer kieContainer = kieServices.newKieContainer(releaseId);
        KieSession session = kieContainer.newKieSession();

        //同一个fact第一次不命中
        try {
            session.fireAllRules();
        } finally {
            session.dispose();
        }
        System.out.println("-----first fire end-------");

        //新增一个规则文件
        kJar = createKieJar(kieServices, releaseId, new ResourceWrapper(ResourceFactory.newByteArrayResource(RULES.getBytes()), RULES_FILE_NAME),"base1.drl");
        repository.addKieModule(kJar);
        kieContainer.updateToVersion(releaseId);

        //同一个fact再次过滤规则：命中
        session = kieContainer.newKieSession();
        try {
            session.fireAllRules();
        } finally {
            session.dispose();
        }
        System.out.println("-----second fire end-------");
    }

    /**
     * 获取规定目录下的规则文件
     */
    private static List<File> getRuleFiles() {
        List<File> list = new ArrayList<>();
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        if (url == null) {
            return list;
        }
        String filePath = url.getPath();
        File rootDir = new File(filePath);
        File[] files = rootDir.listFiles();
        if (files == null) {
            return list;
        }

        for (File itemFile : files) {
            if (itemFile.isDirectory() && itemFile.getName().equals(RULES_PATH)) {
                File[] childrenFile = itemFile.listFiles();
                if (childrenFile != null) {
                    for (File f : childrenFile) {
                        if (f.getName().endsWith(".drl")) {
                            list.add(f);
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     * 初始化一个kjar：把原有的drl包含进新建的kjar中
     *
     * @param ks
     * @param releaseId
     */
    public static InternalKieModule initKieJar(KieServices ks, ReleaseId releaseId) {
        KieFileSystem kfs = createKieFileSystemWithKProject(ks, true);
        kfs.writePomXML(getPom(releaseId));
        for (File file : getRuleFiles()) {
            kfs.write(DROOLS_BASE_PATH + file.getName(),
                    ResourceFactory.newClassPathResource(RULES_PATH + File.separator + file.getName(), "UTF-8"));
        }
        KieBuilder kieBuilder = ks.newKieBuilder(kfs);
        if (!kieBuilder.buildAll().getResults().getMessages().isEmpty()) {
            throw new IllegalStateException("Error creating KieBuilder.");
        }
        return (InternalKieModule) kieBuilder.getKieModule();
    }

    /**
     * 生成jar包,根据原数据进行copy
     * @param ks service
     * @param releaseId 版本
     * @param resourceWrapper 包装
     * @param removePath 删除路径
     * @return kieModule
     */
    public static InternalKieModule createKieJar(KieServices ks, ReleaseId releaseId, ResourceWrapper resourceWrapper,String... removePath) {
        final MemoryKieModule kieModule = (MemoryKieModule) ks.getRepository().getKieModule(releaseId);
        final Map<String, byte[]> map = kieModule.getMemoryFileSystem().getMap();
        KieFileSystem kfs = createKieFileSystemWithKProject(ks, true);
        kfs.writePomXML(getPom(releaseId));
        for (String path : map.keySet()) {
            if (path.endsWith(".drl")) {
                boolean remove = false;
                if (null != removePath) {
                    final Optional<String> any = Arrays.stream(removePath).filter(path::endsWith).findAny();
                    remove = any.isPresent();
                }
                if (!remove) {
                    kfs.write(DROOLS_BASE_PATH + path, map.get(path));
                }
            }
        }
        kfs.write(DROOLS_BASE_PATH + resourceWrapper.getTargetResourceName(), resourceWrapper.getResource());
        KieBuilder kieBuilder = ks.newKieBuilder(kfs);
        if (!kieBuilder.getResults().getMessages().isEmpty()) {
            System.out.println(kieBuilder.getResults().getMessages());
            throw new IllegalStateException("Error creating KieBuilder.");
        }
        return (InternalKieModule) kieBuilder.getKieModule();
    }

    /**
     * 创建默认的kbase和stateful的kiesession
     *
     * @param ks
     * @param isDefault
     * @return
     */
    public static KieFileSystem createKieFileSystemWithKProject(KieServices ks, boolean isDefault) {
        KieModuleModel kproj = ks.newKieModuleModel();
        KieBaseModel kieBaseModel = kproj.newKieBaseModel("KBase").setDefault(isDefault)
                .setEqualsBehavior(EqualityBehaviorOption.EQUALITY).setEventProcessingMode(EventProcessingOption.STREAM);
        // Configure the KieSession.
        kieBaseModel.newKieSessionModel("KSession").setDefault(isDefault)
                .setType(KieSessionModel.KieSessionType.STATEFUL);
        KieFileSystem kfs = ks.newKieFileSystem();
        kfs.writeKModuleXML(kproj.toXML());
        return kfs;
    }

    /**
     * 创建kjar的pom
     *
     * @param releaseId    maven
     * @param dependencies 依赖
     * @return
     */
    public static String getPom(ReleaseId releaseId, ReleaseId... dependencies) {
        StringBuilder pom = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
                + "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n"
                + "  <modelVersion>4.0.0</modelVersion>\n" + "\n" + "  <groupId>" + releaseId.getGroupId()
                + "</groupId>\n" + "  <artifactId>" + releaseId.getArtifactId() + "</artifactId>\n" + "  <version>"
                + releaseId.getVersion() + "</version>\n" + "\n");
        if (dependencies != null && dependencies.length > 0) {
            pom.append("<dependencies>\n");
            for (ReleaseId dep : dependencies) {
                pom.append("<dependency>\n");
                pom.append("  <groupId>").append(dep.getGroupId()).append("</groupId>\n");
                pom.append("  <artifactId>").append(dep.getArtifactId()).append("</artifactId>\n");
                pom.append("  <version>").append(dep.getVersion()).append("</version>\n");
                pom.append("</dependency>\n");
            }
            pom.append("</dependencies>\n");
        }
        pom.append("</project>");
        return pom.toString();
    }

    static class ResourceWrapper {
        private Resource resource;

        private String targetResourceName;

        public ResourceWrapper(Resource resource, String targetResourceName) {
            this.resource = resource;
            this.targetResourceName = targetResourceName;
        }

        public Resource getResource() {
            return resource;
        }

        public String getTargetResourceName() {
            return targetResourceName;
        }

        public void setResource(Resource resource) {
            this.resource = resource;
        }

        public void setTargetResourceName(String targetResourceName) {
            this.targetResourceName = targetResourceName;
        }
    }

}
