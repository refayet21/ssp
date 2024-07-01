import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/person">
        <Translate contentKey="global.menu.entities.person" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/address">
        <Translate contentKey="global.menu.entities.address" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/division">
        <Translate contentKey="global.menu.entities.division" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/district">
        <Translate contentKey="global.menu.entities.district" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/upazila">
        <Translate contentKey="global.menu.entities.upazila" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/rmo">
        <Translate contentKey="global.menu.entities.rmo" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/city-corp-poura">
        <Translate contentKey="global.menu.entities.cityCorpPoura" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/union">
        <Translate contentKey="global.menu.entities.union" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/ward">
        <Translate contentKey="global.menu.entities.ward" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/post-office">
        <Translate contentKey="global.menu.entities.postOffice" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/country">
        <Translate contentKey="global.menu.entities.country" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/document">
        <Translate contentKey="global.menu.entities.document" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/document-type">
        <Translate contentKey="global.menu.entities.documentType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/agency">
        <Translate contentKey="global.menu.entities.agency" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/agency-type">
        <Translate contentKey="global.menu.entities.agencyType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/assignment">
        <Translate contentKey="global.menu.entities.assignment" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/designation">
        <Translate contentKey="global.menu.entities.designation" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/pass-type">
        <Translate contentKey="global.menu.entities.passType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/pass">
        <Translate contentKey="global.menu.entities.pass" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/zone">
        <Translate contentKey="global.menu.entities.zone" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/gate">
        <Translate contentKey="global.menu.entities.gate" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/lane">
        <Translate contentKey="global.menu.entities.lane" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/access-profile">
        <Translate contentKey="global.menu.entities.accessProfile" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/vehicle">
        <Translate contentKey="global.menu.entities.vehicle" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/vehicle-assignment">
        <Translate contentKey="global.menu.entities.vehicleAssignment" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/vehicle-type">
        <Translate contentKey="global.menu.entities.vehicleType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/entry-log">
        <Translate contentKey="global.menu.entities.entryLog" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/agency-license">
        <Translate contentKey="global.menu.entities.agencyLicense" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/department">
        <Translate contentKey="global.menu.entities.department" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/agency-license-type">
        <Translate contentKey="global.menu.entities.agencyLicenseType" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
