import React from "react";
import scss from "./header.module.scss";
import { clsx } from "@/utils/string";

import { FaFileAlt, FaPowerOff } from "react-icons/fa";
import { CgProfile } from "react-icons/cg";
import { CiFileOn } from "react-icons/ci";
import { Button, Drawer, Menu, Modal, Title } from "@mantine/core";
import { useDisclosure } from "@mantine/hooks";
import { useRouter } from "next/router";

import { FaKey } from "react-icons/fa6";
import { IoIosMenu } from "react-icons/io";

import { IoPersonAddSharp } from "react-icons/io5";
import { useGlobalStore } from "@/stores/global-store";
import { ROLE, useUserStore } from "@/stores/user-store";
import Switch from "@/components/Switch/Switch";

function Header() {
  const header = useGlobalStore.use.selectedHeader();
  const router = useRouter();
  const user = useUserStore.use.user();
  // console.log("uiser", user);
  const [password, { open: openPassword, close: closePassword }] =
    useDisclosure(false);
  const [addadmin, { open: openAddAdmin, close: closeAddAdmin }] =
    useDisclosure(false);
  const [opened, { open, close }] = useDisclosure(false);
  const logout = () => {
    // window.localStorage.clear();
    window.sessionStorage.clear();
    if (user?.roles[0]?.roleId === ROLE.ADMIN) {
      window.open("/EPFORecruitment/admin-login", "_self");
    } else if (user?.roles[0]?.roleId === ROLE.SUPERADMIN) {
      window.open("/EPFORecruitment/super-admin-login", "_self");
    } else {
      window.open("/EPFORecruitment/login", "_self");
    }
  };
  const handleprofile = () => {
    router.push("/profile");
    close();
  };

  const handlepaswword = () => {
    openPassword();
    close();
  };
  const handleAddAdmin = () => {
    openAddAdmin();
    close();
  };

  return (
    <header>
      <div
        className={clsx(
          scss.header,
          "px-4 py-2 flex justify-between items-center"
        )}
      >
        <img
          className={scss.logo}
          // onClick={() => {
          //   router.push("/dashboard");
          // }}
          src="/EPFORecruitment/assets/logo/EPFO_LOGO.png"
          alt="EPFO LOGO"
        />

        <Title
          style={{ fontWeight: "400" }}
          order={2}
          className={scss.Header_Title}
        >
          <b>{header}</b>
        </Title>
        <div className={scss.header_content}>
          <span className={scss.header_switch}>
            <Switch />
          </span>

          <Drawer
            opened={opened}
            onClose={close}
            title={
              <>
                <div className="flex align-items-center justify-center">
                  {/* <Switch /> */}
                  <span className={scss.userrr}>
                    {user.username} ({user.employeeId})
                  </span>
                </div>
              </>
            }
            position="right"
            // size="60%"
            overlayProps={{ backgroundOpacity: 0.5, blur: 4 }}
          >
            <div className="flex flex-column my-4">
              {/* {user.roles[0].roleId != ROLE.ADMIN && (
                <div className="flex flex-row align-items-center  my-1">
                  <CgProfile className="mx-2" size={15} />
                  <span onClick={handleprofile}>Profile</span>
                </div>
              )} */}

              <div className="flex flex-row align-items-center my-1">
                <FaKey className="mx-2" size={15} />
                <span onClick={handlepaswword}>Change Password</span>
              </div>

              <div className="flex flex-row align-items-center my-1">
                <FaPowerOff className="mx-2" size={15} />
                <span onClick={logout}>Logout</span>
              </div>
            </div>
          </Drawer>

          <span className={scss.menuIcon} onClick={open}>
            <IoIosMenu size={30} />
          </span>
          {user?.roles?.[0]?.roleId == ROLE.SUPERADMIN && (
            <div className=" border p-1 rounded mr-2">
              <Button
                style={{ backgroundColor: "#05395c" }}
                onClick={handleAddAdmin}
              >
                Add Admin <IoPersonAddSharp className="mx-2" size={20} />
              </Button>
            </div>
          )}
          {user?.roles[0]?.roleId == ROLE.ADMIN &&
            header == "Young Professional Law (EPFO)" && (
              <div className={scss.Header_Profile}>
                <div className="flex">
                  <div className="flex items-center border p-1 rounded">
                    <Menu width="Target" shadow="md" withArrow>
                      <Menu.Target>
                        <Button
                          className="flex "
                          style={{ backgroundColor: "#05395c" }}
                        >
                          Reports
                          <FaFileAlt className="mx-2" size={20} />
                        </Button>
                      </Menu.Target>

                      <Menu.Dropdown>
                        <Menu.Item
                          leftSection={<CiFileOn size={15} />}
                          onClick={() => router.push("/reports/marks-report")}
                        >
                          Marks Report
                        </Menu.Item>
                        <Menu.Item
                          leftSection={<CiFileOn size={15} />}
                          onClick={() =>
                            router.push("/reports/institute-report")
                          }
                        >
                          Institute Report
                        </Menu.Item>
                      </Menu.Dropdown>
                    </Menu>
                  </div>
                </div>
              </div>
            )}

          <div className={scss.Header_Profile}>
            <div className="flex items-center border p-1 rounded">
              <Menu width="Target" shadow="md" withArrow>
                <Menu.Target>
                  <Button
                    className="flex "
                    style={{ backgroundColor: "#05395c" }}
                  >
                    <span>{user.username}</span>
                    <CgProfile className="mx-2" size={20} />
                  </Button>
                </Menu.Target>

                <Menu.Dropdown>
                  {/* {user.roles[0].roleId != ROLE.ADMIN && (
                    <Menu.Item
                      leftSection={<CgProfile size={20} />}
                      onClick={() => router.push("/profile")}
                    >
                      Profile
                    </Menu.Item>
                  )} */}
                  <Menu.Item
                    leftSection={<FaKey size={15} />}
                    onClick={openPassword}
                  >
                    Change Password
                  </Menu.Item>
                  <Menu.Item leftSection={<FaPowerOff />} onClick={logout}>
                    Logout
                  </Menu.Item>
                </Menu.Dropdown>
              </Menu>
            </div>
          </div>
        </div>
      </div>

      {/* <Modal
        opened={password}
        onClose={closePassword}
        size="30rem"
        withCloseButton={true}
        title="Change Password"
      >
        <ChangePassword onClose={closePassword} />
      </Modal>

      <Modal
        opened={addadmin}
        onClose={closeAddAdmin}
        size="30rem"
        withCloseButton={true}
        title="Add Admin"
      >
        <AddAdmin onClose={closeAddAdmin} />
      </Modal> */}
    </header>
  );
}

export default Header;
