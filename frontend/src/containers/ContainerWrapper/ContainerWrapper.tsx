// ContainerWrapper.tsx


import React from 'react';
import "./ContainerWrapper.scss";

interface ContainerWrapperProps {
  children: React.ReactNode;
}

const ContainerWrapper: React.FC<ContainerWrapperProps> = ({ children }) => {
  return <div className="container-wrapper">{children}</div>;
};

export default ContainerWrapper;
