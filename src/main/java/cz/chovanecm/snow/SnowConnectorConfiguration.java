package cz.chovanecm.snow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class SnowConnectorConfiguration {
    @NonNull
    private String serviceNowDomainName;
    private String username;
    // TODO: a better way of storing password?
    private String password;
    private String proxyServerAddress;
    private int proxyServerPort;

    public boolean isProxySet() {
        return getProxyServerPort() > 0 && getProxyServerAddress() != null && !"".equals(getProxyServerAddress());
    }
}
