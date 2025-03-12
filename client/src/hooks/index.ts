import { useEffect, useRef, useState } from 'react';
import { useLocation } from 'react-router';

export const useTitle = (title: string) => {
  const documentDefined = typeof document !== 'undefined';
  const originalTitle = useRef(documentDefined ? document.title : null);

  useEffect(() => {
    if (!documentDefined) return;

    if (document.title !== title) {
      document.title = title;
    }

    return () => {
      document.title = originalTitle.current || '';
    };
  }, [title]);
};

export const useReactRouterStateFrom = (defaultValue: string) => {
  const location = useLocation();
  const [from, setFrom] = useState(defaultValue);

  useEffect(() => {
    if (location.state?.from && location.state.from !== from) {
      setFrom(location.state.from);
    } else {
      setFrom(defaultValue);
    }
  }, [location.state, defaultValue]);

  return from;
};
