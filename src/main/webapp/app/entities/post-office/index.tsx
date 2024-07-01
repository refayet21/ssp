import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PostOffice from './post-office';
import PostOfficeDetail from './post-office-detail';
import PostOfficeUpdate from './post-office-update';
import PostOfficeDeleteDialog from './post-office-delete-dialog';

const PostOfficeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PostOffice />} />
    <Route path="new" element={<PostOfficeUpdate />} />
    <Route path=":id">
      <Route index element={<PostOfficeDetail />} />
      <Route path="edit" element={<PostOfficeUpdate />} />
      <Route path="delete" element={<PostOfficeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PostOfficeRoutes;
