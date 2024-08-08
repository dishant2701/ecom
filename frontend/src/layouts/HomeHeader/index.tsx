import React from "react";
import scss from "./homeheader.module.scss";
import { Button } from "@mantine/core";

const HomeHeader = () => {
  return (
    <>
      <header>
        <div className={scss.header}>
          <div className={scss.image}>
            <img
              src="/EPFORecruitment/assets/images/epfo-logo.png"
              alt="EPFO LOGO"
            />
          </div>
          <div className={scss.heading}>
            <h4> E-Comm</h4>
          </div>
        </div>
      </header>
    </>
  );
};

export default HomeHeader;
