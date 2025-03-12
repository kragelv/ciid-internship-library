import { ComponentProps } from 'react';
import { useLocation, Link, Location } from 'react-router';

type StateFromLinkProps = ComponentProps<typeof Link>;

export const reactRouterLocationToFullHref = (location: Location) => {
  const { pathname, search, hash } = location;
  return `${pathname}${search}${hash}`;
};

const StateFromLink = (props: StateFromLinkProps) => {
  const location = useLocation();

  return (
    <Link
      {...props}
      state={{ ...props.state, from: reactRouterLocationToFullHref(location) }}
    ></Link>
  );
};

export default StateFromLink;
