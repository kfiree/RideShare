import { useState, useEffect, useCallback, useContext } from 'react';
import { AuthContext } from '../context/auth';

export const useFetchData = func => {
  const { user, token } = useContext(AuthContext);

  const [state, setState] = useState(null);
  const [error, setError] = useState(false);

  const userAction = {
    userId: user.id,
    token: token,
  };




  const fetchData = useCallback(async () => {
    // console.log('API CALLL');
    setError(false);
    try {
      const res = await func(userAction);
      console.log('res', res);
      if (res?.errors) {
        throw new Error(res?.errors);
      } else {
        setState(res);
      }
    } catch (error) {
      setError(true);
    }
  }, [user, token, func]);

  useEffect(() => {
    fetchData();
    return () => {
      setState({});
    };
  }, [fetchData]);

  return { state, error, fetchData, setState };
};
