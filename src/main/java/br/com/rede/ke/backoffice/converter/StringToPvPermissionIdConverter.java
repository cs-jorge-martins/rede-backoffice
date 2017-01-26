package br.com.rede.ke.backoffice.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermissionId;

@Component
public class StringToPvPermissionIdConverter implements Converter<String, PvPermissionId> {
    @Override
    public PvPermissionId convert(String source) {
        String[] ids = source.split("-");
        return new PvPermissionId(Long.parseLong(ids[0]), Long.parseLong(ids[1]));
    }
}