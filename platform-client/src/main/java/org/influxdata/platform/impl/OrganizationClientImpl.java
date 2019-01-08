/*
 * The MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.influxdata.platform.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.influxdata.platform.Arguments;
import org.influxdata.platform.OrganizationClient;
import org.influxdata.platform.domain.Organization;
import org.influxdata.platform.domain.Organizations;
import org.influxdata.platform.domain.ResourceMember;
import org.influxdata.platform.domain.ResourceMembers;
import org.influxdata.platform.domain.Secrets;
import org.influxdata.platform.domain.User;
import org.influxdata.platform.error.rest.NotFoundException;
import org.influxdata.platform.rest.AbstractRestClient;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import retrofit2.Call;

/**
 * @author Jakub Bednar (bednar@github) (12/09/2018 08:57)
 */
final class OrganizationClientImpl extends AbstractRestClient implements OrganizationClient {

    private static final Logger LOG = Logger.getLogger(OrganizationClientImpl.class.getName());

    private final PlatformService platformService;
    private final JsonAdapter<Organization> adapter;
    private final JsonAdapter<User> userAdapter;
    private final JsonAdapter<Map> mapAdapter;
    private final JsonAdapter<List> listAdapter;

    OrganizationClientImpl(@Nonnull final PlatformService platformService, @Nonnull final Moshi moshi) {

        Arguments.checkNotNull(platformService, "PlatformService");
        Arguments.checkNotNull(moshi, "Moshi to create adapter");

        this.platformService = platformService;
        this.adapter = moshi.adapter(Organization.class);
        this.userAdapter = moshi.adapter(User.class);
        this.mapAdapter = moshi.adapter(Map.class);
        this.listAdapter = moshi.adapter(List.class);
    }

    @Nullable
    @Override
    public Organization findOrganizationByID(@Nonnull final String organizationID) {

        Arguments.checkNonEmpty(organizationID, "Organization ID");

        Call<Organization> organization = platformService.findOrganizationByID(organizationID);

        return execute(organization, NotFoundException.class);
    }

    @Nonnull
    @Override
    public List<Organization> findOrganizations() {

        Call<Organizations> organizationsCall = platformService.findOrganizations();

        Organizations organizations = execute(organizationsCall);
        LOG.log(Level.FINEST, "findOrganizations found: {0}", organizations);

        return organizations.getOrgs();
    }

    @Nonnull
    @Override
    public Organization createOrganization(@Nonnull final String name) {

        Arguments.checkNonEmpty(name, "Organization name");

        Organization organization = new Organization();
        organization.setName(name);

        return createOrganization(organization);
    }

    @Nonnull
    @Override
    public Organization createOrganization(@Nonnull final Organization organization) {

        Arguments.checkNotNull(organization, "Organization");

        String json = adapter.toJson(organization);

        Call<Organization> call = platformService.createOrganization(createBody(json));

        return execute(call);
    }

    @Nonnull
    @Override
    public Organization updateOrganization(@Nonnull final Organization organization) {

        Arguments.checkNotNull(organization, "Organization");

        String json = adapter.toJson(organization);

        Call<Organization> orgCall = platformService.updateOrganization(organization.getId(), createBody(json));

        return execute(orgCall);
    }

    @Override
    public void deleteOrganization(@Nonnull final Organization organization) {

        Objects.requireNonNull(organization, "Organization is required");

        deleteOrganization(organization.getId());
    }

    @Override
    public void deleteOrganization(@Nonnull final String organizationID) {

        Arguments.checkNonEmpty(organizationID, "Organization ID");

        Call<Void> call = platformService.deleteOrganization(organizationID);
        execute(call);
    }

    @Override
    public List<String> getSecrets(@Nonnull final Organization organization) {

        Arguments.checkNotNull(organization, "Organization");

        return getSecrets(organization.getId());
    }

    @Override
    public List<String> getSecrets(@Nonnull final String organizationID) {

        Arguments.checkNonEmpty(organizationID, "Organization ID");

        Call<Secrets> call = platformService.getSecrets(organizationID);

        Secrets secrets = execute(call);
        LOG.log(Level.FINEST, "getSecrets found: {0}", secrets);

        return secrets.getSecrets();
    }

    @Override
    public void putSecrets(@Nonnull final Map<String, String> secrets, @Nonnull final Organization organization) {

        Arguments.checkNotNull(organization, "Organization");

        putSecrets(secrets, organization.getId());
    }

    @Override
    public void putSecrets(@Nonnull final Map<String, String> secrets, @Nonnull final String organizationID) {

        Arguments.checkNonEmpty(organizationID, "Organization ID");
        Arguments.checkNotNull(secrets, "secrets");

        Call<Void> call = platformService.putSecrets(organizationID, createBody(mapAdapter.toJson(secrets)));
        execute(call);
    }

    @Override
    public void deleteSecrets(@Nonnull final List<String> secrets, @Nonnull final Organization organization) {

        Arguments.checkNotNull(organization, "Organization");
        Arguments.checkNotNull(secrets, "secrets");

        deleteSecrets(secrets, organization.getId());
    }

    @Override
    public void deleteSecrets(@Nonnull final List<String> secrets, @Nonnull final String organizationID) {

        Arguments.checkNonEmpty(organizationID, "Organization ID");
        Arguments.checkNotNull(secrets, "secrets");

        Call<Void> call = platformService.deleteSecrets(organizationID, createBody(listAdapter.toJson(secrets)));
        execute(call);
    }

    @Nonnull
    @Override
    public List<ResourceMember> getMembers(@Nonnull final Organization organization) {

        Arguments.checkNotNull(organization, "Organization");

        return getMembers(organization.getId());
    }

    @Nonnull
    @Override
    public List<ResourceMember> getMembers(@Nonnull final String organizationID) {

        Arguments.checkNonEmpty(organizationID, "Organization ID");

        Call<ResourceMembers> call = platformService.findOrganizationMembers(organizationID);
        ResourceMembers resourceMembers = execute(call);
        LOG.log(Level.FINEST, "findOrganizationMembers found: {0}", resourceMembers);

        return resourceMembers.getUsers();
    }

    @Nonnull
    @Override
    public ResourceMember addMember(@Nonnull final User member, @Nonnull final Organization organization) {

        Arguments.checkNotNull(organization, "Organization");
        Arguments.checkNotNull(member, "member");

        return addMember(member.getId(), organization.getId());
    }

    @Nonnull
    @Override
    public ResourceMember addMember(@Nonnull final String memberID, @Nonnull final String organizationID) {

        Arguments.checkNonEmpty(memberID, "Member ID");
        Arguments.checkNonEmpty(organizationID, "Organization ID");

        User user = new User();
        user.setId(memberID);

        String json = userAdapter.toJson(user);
        Call<ResourceMember> call = platformService.addOrganizationMember(organizationID, createBody(json));

        return execute(call);
    }

    @Override
    public void deleteMember(@Nonnull final User member, @Nonnull final Organization organization) {

        Arguments.checkNotNull(organization, "Organization");
        Arguments.checkNotNull(member, "member");

        deleteMember(member.getId(), organization.getId());
    }

    @Override
    public void deleteMember(@Nonnull final String memberID, @Nonnull final String organizationID) {

        Arguments.checkNonEmpty(memberID, "Member ID");
        Arguments.checkNonEmpty(organizationID, "Organization ID");

        Call<Void> call = platformService.deleteOrganizationMember(organizationID, memberID);
        execute(call);
    }

    @Nonnull
    @Override
    public List<ResourceMember> getOwners(@Nonnull final Organization organization) {

        Arguments.checkNotNull(organization, "Organization");

        return getOwners(organization.getId());
    }

    @Nonnull
    @Override
    public List<ResourceMember> getOwners(@Nonnull final String organizationID) {

        Arguments.checkNonEmpty(organizationID, "Organization ID");

        Call<ResourceMembers> call = platformService.findOrganizationOwners(organizationID);
        ResourceMembers resourceMembers = execute(call);
        LOG.log(Level.FINEST, "findOrganizationOwners found: {0}", resourceMembers);

        return resourceMembers.getUsers();
    }

    @Nonnull
    @Override
    public ResourceMember addOwner(@Nonnull final User owner, @Nonnull final Organization organization) {

        Arguments.checkNotNull(organization, "Organization");
        Arguments.checkNotNull(owner, "owner");

        return addOwner(owner.getId(), organization.getId());
    }

    @Nonnull
    @Override
    public ResourceMember addOwner(@Nonnull final String ownerID, @Nonnull final String organizationID) {

        Arguments.checkNonEmpty(ownerID, "Owner ID");
        Arguments.checkNonEmpty(organizationID, "Organization ID");

        User user = new User();
        user.setId(ownerID);

        String json = userAdapter.toJson(user);
        Call<ResourceMember> call = platformService.addOrganizationOwner(organizationID, createBody(json));

        return execute(call);
    }

    @Override
    public void deleteOwner(@Nonnull final User owner, @Nonnull final Organization organization) {
        Arguments.checkNotNull(organization, "Organization");
        Arguments.checkNotNull(owner, "owner");

        deleteOwner(owner.getId(), organization.getId());
    }

    @Override
    public void deleteOwner(@Nonnull final String ownerID, @Nonnull final String organizationID) {

        Arguments.checkNonEmpty(ownerID, "Owner ID");
        Arguments.checkNonEmpty(organizationID, "Organization ID");

        Call<Void> call = platformService.deleteOrganizationOwner(organizationID, ownerID);
        execute(call);
    }
}