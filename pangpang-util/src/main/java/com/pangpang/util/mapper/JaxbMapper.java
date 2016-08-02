package com.pangpang.util.mapper;

import com.pangpang.util.exception.Exceptions;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.namespace.QName;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;

/**
 * Created by jiangjg on 2016/8/2.
 */
public class JaxbMapper {
    //多线程安全的Context.
    private JAXBContext jaxbContext;

    /**
     * @param rootTypes 所有需要序列化的Root对象的Class.
     */
    public JaxbMapper(Class<?>... rootTypes) {
        try {
            jaxbContext = JAXBContext.newInstance(rootTypes);
        } catch (JAXBException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * Java Object->Xml without encoding.
     */
    public String toXml(Object root) {
        return toXml(root, null);
    }

    /**
     * Java Object->Xml with encoding.
     */
    public String toXml(Object root, String encoding) {
        try {
            StringWriter writer = new StringWriter();
            createMarshaller(encoding).marshal(root, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * Java Object->Xml without encoding, 特别支持Root Element是Collection的情形.
     */
    public String toXml(Collection<?> root, String rootName) {
        return toXml(root, rootName, null);
    }

    /**
     * Java Object->Xml with encoding, 特别支持Root Element是Collection的情形.
     */
    public String toXml(Collection<?> root, String rootName, String encoding) {
        try {
            CollectionWrapper wrapper = new CollectionWrapper();
            wrapper.collection = root;

            JAXBElement<CollectionWrapper> wrapperElement = new JAXBElement<CollectionWrapper>(new QName(rootName),
                    CollectionWrapper.class, wrapper);

            StringWriter writer = new StringWriter();
            createMarshaller(encoding).marshal(wrapperElement, writer);

            return writer.toString();
        } catch (JAXBException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * Xml->Java Object.
     */
    public <T> T fromXml(String xml) {
        try {
            StringReader reader = new StringReader(xml);
            return (T) createUnmarshaller().unmarshal(reader);
        } catch (JAXBException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 创建Marshaller并设定encoding(可为null).
     */
    public Marshaller createMarshaller(String encoding) {
        try {
            Marshaller marshaller = jaxbContext.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            if (StringUtils.isNotBlank(encoding)) {
                marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            }

            return marshaller;
        } catch (JAXBException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 创建UnMarshaller.
     */
    public Unmarshaller createUnmarshaller() {
        try {
            return jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 封装Root Element 是 Collection的情况.
     */
    public static class CollectionWrapper {

        @XmlAnyElement
        protected Collection<?> collection;
    }
}
