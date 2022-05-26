import { useState } from 'react';

export const useForm = (callback, initialState = {}) => {
  const [values, setValues] = useState(initialState);

  const onChange = event => {
    setValues({ ...values, [event.target.name]: event.target.value });
  };

  const setFieldValue = (name, value) => {
    setValues({ ...values, [name]: value });
  };

  const onSubmit = event => {
    event.preventDefault();
    callback();
  };

  const onOfficeEquipmentChange = event => {
    // setValues({
    //   ...values,
    //   officeequipmentforemployees: [
    //     // values.officeequipmentforemployees.map(x => {
    //     // //console.log(x.name !== event.target.name);
    //     // }),
    //     ...values.officeequipmentforemployees,
    //     ([
    //       values.officeequipmentforemployees[event.target.name],
    //     ]: event.target.value),
    //     // { name: event.target.name, sum: event.target.value },
    //   ],
    // });
  };

  return {
    onChange,
    onSubmit,
    setValues,
    setFieldValue,
    onOfficeEquipmentChange,
    values,
  };
};
