import React from "react";

import scss from "./footer.module.scss";
import { clsx } from "@/utils/string";
import { MdCopyright } from "react-icons/md";

const Footer = () => {
  return (
    <footer className={scss.footer_main}>
      <div className={scss.footerpage}>
        <span className="flex items-center">
          <MdCopyright className={clsx(scss.icon_footer, "mx-1")} />
          <span className="mx-1">{new Date().getFullYear()}</span>
          <span> - All Rights Reserved</span>
        </span>
      </div>
    </footer>
  );
};

export default Footer;
